package lol.lazar.lazarkit.panels.server

import com.qualcomm.hardware.lynx.LynxModule
import fi.iki.elonen.NanoWSD
import kotlinx.serialization.PolymorphicSerializer
import lol.lazar.lazarkit.flows.FlowRegistry
import lol.lazar.lazarkit.panels.GlobalData
import lol.lazar.lazarkit.panels.configurables.Configurables
import lol.lazar.lazarkit.panels.integration.OpModeData
import lol.lazar.lazarkit.panels.json.ActiveOpMode
import lol.lazar.lazarkit.panels.json.AllFlowsJson
import lol.lazar.lazarkit.panels.json.BatteryVoltage
import lol.lazar.lazarkit.panels.json.Canvas
import lol.lazar.lazarkit.panels.json.GetActiveOpModeRequest
import lol.lazar.lazarkit.panels.json.GetJvmFieldsRequest
import lol.lazar.lazarkit.panels.json.GetOpModesRequest
import lol.lazar.lazarkit.panels.json.InitOpModeRequest
import lol.lazar.lazarkit.panels.json.JSONData
import lol.lazar.lazarkit.panels.json.ReceivedJvmFields
import lol.lazar.lazarkit.panels.json.ReceivedOpModes
import lol.lazar.lazarkit.panels.json.StartActiveOpModeRequest
import lol.lazar.lazarkit.panels.json.StopActiveOpModeRequest
import lol.lazar.lazarkit.panels.json.TelemetryPacket
import lol.lazar.lazarkit.panels.json.TestObject
import lol.lazar.lazarkit.panels.json.TimeObject
import lol.lazar.lazarkit.panels.json.UpdatedJvmFields
import lol.lazar.lazarkit.panels.json.json
import lol.lazar.lazarkit.panels.json.toJson
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import kotlin.math.max

class Socket(
    private val initOpMode: (name: String) -> Unit,
    private val startOpMode: () -> Unit,
    private val stopOpMode: () -> Unit,
) : NanoWSD(8002) {
    override fun openWebSocket(handshake: IHTTPSession): WebSocket {
        return TimeWebSocket(handshake)
    }

    private val clients: MutableSet<TimeWebSocket> = mutableSetOf()

    fun send(data: JSONData) {
        if (!isAlive) return
        for (client in clients) {
            try {
                client.send(data)
            } catch (e: IOException) {
                println("DASH: Error sending message to client: ${e.message}")
            }
        }
    }

    fun sendAllClients(data: JSONData) = send(data)

    fun broadcastActiveOpMode() {
        if (!isAlive) return
        for (client in clients) {
            try {
                client.sendActiveOpMode()
            } catch (e: IOException) {
                println("DASH: Error sending message to client: ${e.message}")
            }
        }
    }

    fun sendTest() {
        send(TestObject(data = "info"))
    }

    fun sendOpModesList() {
        send(ReceivedOpModes(GlobalData.opModeList))
    }

    fun sendTelemetry(lines: List<String>, canvas: Canvas) {
        if (!isAlive) return
        println("DASH: sent telemetry")
        for (client in clients) {
            client.send(TelemetryPacket(lines, canvas))
        }
    }

    private inner class TimeWebSocket(handshake: IHTTPSession) : WebSocket(handshake) {
        private var timer: Timer = Timer()

        private var ping: TimerTask? = null

        fun send(data: JSONData) {
            try {
                send(data.toJson())
            } catch (e: IOException) {
                println("DASH: Error sending message to client: ${e.message}")
            }
        }

        override fun onOpen() {
            startSendingTime()

            clients.add(this)

            if (ping == null) {
                ping = object : TimerTask() {
                    override fun run() {
                        try {
                            ping("LAZAR".toByteArray())
                        } catch (e: IOException) {
                            ping!!.cancel()
                        }
                    }
                }
                Timer().schedule(ping, 1000, 2000)
            }

            sendOpModesList()
            sendActiveOpMode()
            sendJvmFields()
            sendAllFlows()
        }

        var lastBatteryVoltage: Double = 0.0

        fun startSendingTime() {
            timer.schedule(object : TimerTask() {
                override fun run() {
                    try {
                        val time = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(Date())
                        send(TimeObject(time = time))
                        updateBatteryVoltage()
                        if (GlobalData.batteryVoltage != lastBatteryVoltage) {
                            send(BatteryVoltage(GlobalData.batteryVoltage))
                            lastBatteryVoltage = GlobalData.batteryVoltage
                        }
                    } catch (e: IOException) {
                        stopTimer()
                    }
                }
            }, 0, 1000)
        }

        fun stopTimer() {
            timer.cancel()
            timer.purge()
        }

        fun updateBatteryVoltage() {
            GlobalData.batteryVoltage = -1.0
            val hardwareMap = GlobalData.activeOpMode?.hardwareMap ?: return
            for (module in hardwareMap.getAll(LynxModule::class.java)) {
                GlobalData.batteryVoltage =
                    max(GlobalData.batteryVoltage, module.getInputVoltage(VoltageUnit.VOLTS))
            }
        }


        override fun onClose(
            code: WebSocketFrame.CloseCode,
            reason: String,
            initiatedByRemote: Boolean
        ) {
            println("DASH: WebSocket closed: $code, reason: $reason")
            stopTimer()
            ping?.cancel();

            clients.remove(this)
        }

        fun sendActiveOpMode() {
            val status = when (GlobalData.status) {
                OpModeData.OpModeStatus.INIT -> "init"
                OpModeData.OpModeStatus.RUNNING -> "running"
                OpModeData.OpModeStatus.STOPPED -> "stopped"
            }
            send(
                ActiveOpMode(
                    GlobalData.activeOpModeInfo,
                    status
                )
            )
        }

        fun sendOpModesList() {
            send(ReceivedOpModes(GlobalData.opModeList))
        }

        fun sendJvmFields() {
            send(ReceivedJvmFields(Configurables.jvmFields.map { it.toJsonType }))
        }

        fun sendAllFlows() {
            send(AllFlowsJson(FlowRegistry.allFlowsJson))
        }

        override fun onMessage(message: WebSocketFrame) {
            println("DASH: Received message: ${message.textPayload}")
            try {
                val decoded = json.decodeFromString(
                    PolymorphicSerializer(JSONData::class),
                    message.textPayload
                )
                when (decoded) {
                    is GetOpModesRequest -> {
                        sendOpModesList()
                    }

                    is GetActiveOpModeRequest -> {
                        sendActiveOpMode()
                    }

                    is InitOpModeRequest -> {
                        initOpMode(decoded.opModeName)
                    }

                    is StartActiveOpModeRequest -> {
                        startOpMode()
                    }

                    is StopActiveOpModeRequest -> {
                        stopOpMode()
                    }

                    is GetJvmFieldsRequest -> {
                        sendJvmFields()
                    }

                    is UpdatedJvmFields -> {
                        println("DASH: Received JvmFields: ${decoded.fields}")


                        decoded.fields.forEach {
                            println("DASH: Field id: ${it.id}, New value: ${it.newValueString}")
                            val generalRef = Configurables.fieldsMap[it.id] ?: return
                            val convertedValue = generalRef.convertValue(it.newValueString)
                            generalRef.currentValue = convertedValue
                        }

                        sendAllClients(
                            UpdatedJvmFields(
                                decoded.fields
                            )
                        )
                    }

                    else -> {
                        println("DASH: Unknown message type: ${decoded::class.simpleName}")
                    }
                }
            } catch (e: Exception) {
                println("DASH: Error decoding JSON: ${e.message}")
                close(
                    WebSocketFrame.CloseCode.InternalServerError,
                    "Error in message handling",
                    false
                )
            }
        }

        override fun onPong(pong: WebSocketFrame) {}

        override fun onException(exception: IOException) {
            stopTimer()
        }
    }

    fun startServer() {
        start()
        println("DASH: Socket started on port 8002")
    }

    fun stopServer() {
        stop()
        println("DASH: Socket stopped")
    }
}
