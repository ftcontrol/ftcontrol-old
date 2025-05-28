package com.bylazar.ftcontrol.panels.server

import com.bylazar.ftcontrol.panels.GlobalData
import com.bylazar.ftcontrol.panels.Logger
import com.bylazar.ftcontrol.panels.configurables.ConfigurablesManager
import com.bylazar.ftcontrol.panels.integration.OpModeData
import com.bylazar.ftcontrol.panels.json.ActiveOpMode
import com.bylazar.ftcontrol.panels.json.BatteryVoltage
import com.bylazar.ftcontrol.panels.json.Canvas
import com.bylazar.ftcontrol.panels.json.GetActiveOpModeRequest
import com.bylazar.ftcontrol.panels.json.GetJvmFieldsRequest
import com.bylazar.ftcontrol.panels.json.GetOpModesRequest
import com.bylazar.ftcontrol.panels.json.GraphPacket
import com.bylazar.ftcontrol.panels.json.InitOpModeRequest
import com.bylazar.ftcontrol.panels.json.JSONData
import com.bylazar.ftcontrol.panels.json.PluginAction
import com.bylazar.ftcontrol.panels.json.PluginsUpdate
import com.bylazar.ftcontrol.panels.json.ReceivedInitialJvmFields
import com.bylazar.ftcontrol.panels.json.ReceivedJvmFields
import com.bylazar.ftcontrol.panels.json.ReceivedOpModes
import com.bylazar.ftcontrol.panels.json.ReceivedPlugins
import com.bylazar.ftcontrol.panels.json.StartActiveOpModeRequest
import com.bylazar.ftcontrol.panels.json.StopActiveOpModeRequest
import com.bylazar.ftcontrol.panels.json.TelemetryCanvasPacket
import com.bylazar.ftcontrol.panels.json.TelemetryGraphPacket
import com.bylazar.ftcontrol.panels.json.TelemetryLinesPacket
import com.bylazar.ftcontrol.panels.json.TestObject
import com.bylazar.ftcontrol.panels.json.TimeObject
import com.bylazar.ftcontrol.panels.json.UpdatedJvmFields
import com.bylazar.ftcontrol.panels.json.json
import com.bylazar.ftcontrol.panels.json.toJson
import com.bylazar.ftcontrol.panels.plugins.PluginManager
import com.qualcomm.hardware.lynx.LynxModule
import fi.iki.elonen.NanoWSD
import kotlinx.serialization.PolymorphicSerializer
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
                Logger.socketError("Error sending message to client: ${e.message}")
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
                Logger.socketError("Error sending message to client: ${e.message}")
            }
        }
    }

    fun sendTest() {
        send(TestObject(data = "info"))
    }

    fun sendOpModesList() {
        send(ReceivedOpModes(GlobalData.opModeList))
    }

    fun sendConfigurables(){
        if (!isAlive) return
        Logger.socketLog("Sent configurables")
        for (client in clients) {
            client.sendJvmFields()
        }
    }

    fun sendLines(
        lines: MutableList<String>
    ) {
        if (!isAlive) return
        Logger.socketLog("Sent lines")
        for (client in clients) {
            client.send(TelemetryLinesPacket(lines, System.currentTimeMillis()))
        }
    }

    fun sendGraph(
        graph: MutableMap<String, MutableList<GraphPacket>>
    ) {
        if (!isAlive) return
        Logger.socketLog("Sent graph")
        for (client in clients) {
            client.send(TelemetryGraphPacket(graph, System.currentTimeMillis()))
        }
    }

    fun sendCanvas(
        canvas: Canvas,
    ) {
        if (!isAlive) return
        Logger.socketLog("Sent canvas")
        for (client in clients) {
            client.send(TelemetryCanvasPacket(canvas, System.currentTimeMillis()))
        }
    }

    private inner class TimeWebSocket(handshake: IHTTPSession) : WebSocket(handshake) {
        private var timer: Timer = Timer()

        private var ping: TimerTask? = null

        fun send(data: JSONData) {
            try {
                send(data.toJson())
            } catch (e: IOException) {
                Logger.socketError("Error sending message to client: ${e.message}")
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
            sendAllPlugins()
        }

        var lastBatteryVoltage: Double = 0.0

        fun updatePages() {
            //todo: send only changes
            val dynamicPlugins =
                PluginManager.plugins.values.filter { it.globalVariables.isNotEmpty() }
            if (dynamicPlugins.isNotEmpty()) {
                send(PluginsUpdate(plugins = dynamicPlugins.map { it.toJson }))
            }
        }

        fun startSendingTime() {
            timer.schedule(object : TimerTask() {
                override fun run() {
                    try {
                        val time = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(Date())
                        send(TimeObject(time = time))
                        updateBatteryVoltage()
                        updatePages()
                        if (GlobalData.batteryVoltage != lastBatteryVoltage) {
                            send(BatteryVoltage(GlobalData.batteryVoltage))
                            lastBatteryVoltage = GlobalData.batteryVoltage
                        }
                    } catch (e: IOException) {
                        stopTimer()
                    }
                }
            }, 0, 100)
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
            Logger.socketError("WebSocket closed: $code, reason: $reason")
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
            send(ReceivedInitialJvmFields(ConfigurablesManager.initialJvmFields))
            send(ReceivedJvmFields(ConfigurablesManager.jvmFields.map { it.toJsonType }))
        }

        fun sendAllPlugins() {
            send(ReceivedPlugins(PluginManager.plugins.values.map { it.toJson }))
        }

        override fun onMessage(message: WebSocketFrame) {
            Logger.socketLog("Received message: ${message.textPayload}")
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
                        Logger.socketLog("Received JvmFields: ${decoded.fields}")

                        decoded.fields.forEach {
                            Logger.socketLog("Field id: ${it.id}, New value: ${it.newValueString}")
                            val generalRef = ConfigurablesManager.fieldsMap[it.id] ?: return
                            generalRef.setValue(it.newValueString)
                        }

                        sendAllClients(
                            UpdatedJvmFields(
                                decoded.fields
                            )
                        )
                    }

                    is PluginAction -> {
                        PluginManager.plugins[decoded.id]?.actions?.get(decoded.action)?.invoke()
                    }

                    else -> {
                        Logger.socketError("Unknown message type: ${decoded::class.simpleName}")
                    }
                }
            } catch (e: Exception) {
                Logger.socketError("Error decoding JSON: ${e.message}")
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
        Logger.socketLog("Socket started on port 8002")
    }

    fun stopServer() {
        stop()
        Logger.socketLog("Socket stopped")
    }
}
