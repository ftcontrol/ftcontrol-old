package lol.lazar.lazarkit.panels.server

import fi.iki.elonen.NanoWSD
import kotlinx.serialization.PolymorphicSerializer
import lol.lazar.lazarkit.panels.GlobalData
import lol.lazar.lazarkit.panels.OpModeData
import lol.lazar.lazarkit.panels.configurables.GenericType
import lol.lazar.lazarkit.panels.data.ActiveOpMode
import lol.lazar.lazarkit.panels.data.GetActiveOpModeRequest
import lol.lazar.lazarkit.panels.data.GetJvmFieldsRequest
import lol.lazar.lazarkit.panels.data.GetOpModesRequest
import lol.lazar.lazarkit.panels.data.InitOpModeRequest
import lol.lazar.lazarkit.panels.data.JSONData
import lol.lazar.lazarkit.panels.data.ReceivedJvmFields
import lol.lazar.lazarkit.panels.data.ReceivedOpModes
import lol.lazar.lazarkit.panels.data.StartActiveOpModeRequest
import lol.lazar.lazarkit.panels.data.StopActiveOpModeRequest
import lol.lazar.lazarkit.panels.data.TelemetryPacket
import lol.lazar.lazarkit.panels.data.TestObject
import lol.lazar.lazarkit.panels.data.TimeObject
import lol.lazar.lazarkit.panels.data.UpdatedJvmFields
import lol.lazar.lazarkit.panels.data.json
import lol.lazar.lazarkit.panels.data.toJson
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

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

    fun sendTelemetry(lines: List<String>) {
        if (!isAlive) return
        println("DASH: sent telemetry")
        for (client in clients) {
            client.send(TelemetryPacket(lines))
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
        }

        fun startSendingTime() {
            timer.schedule(object : TimerTask() {
                override fun run() {
                    try {
                        val time = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(Date())
                        send(TimeObject(time = time))
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
            send(ReceivedJvmFields(GlobalData.jvmFields.map { it.toJsonType() }))
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

                    is ReceivedJvmFields -> {
                        println("DASH: Received JvmFields: ${decoded.fields}")

                        sendAllClients(
                            UpdatedJvmFields(
                                decoded.fields
                            )
                        )

                        decoded.fields.forEach {
                            val ref = it.toReference() ?: return

                            println("DASH: Found field: ${ref.className}.${ref.name} | Type: ${ref.type}")
                            println("DASH: Value String: ${it.valueString}")
                            println("DASH: Value: ${it.valueAsType}")

                            when (ref.type) {
                                GenericType.Types.INT -> {
                                    ref.reference.set(null, it.valueAsType)
                                }
                                GenericType.Types.DOUBLE -> {
                                    ref.reference.set(null, it.valueAsType)
                                }
                                GenericType.Types.LONG -> {
                                    ref.reference.set(null, it.valueAsType)
                                }
                                GenericType.Types.FLOAT -> {
                                    ref.reference.set(null, it.valueAsType)
                                }
                                GenericType.Types.STRING -> {
                                    ref.reference.set(null, it.valueAsType)
                                }
                                GenericType.Types.BOOLEAN -> {
                                    ref.reference.set(null, it.valueAsType)
                                }

                                else -> {}
                            }

//
//                            if (field != null) {
//
//                                when (it.type) {
//
//                                    GenericType.Types.DOUBLE -> field.reference.set(
//                                        null,
//                                        value.toDouble()
//                                    )
//
//                                    GenericType.Types.STRING -> field.reference.set(null, value)
//                                    GenericType.Types.BOOLEAN -> field.reference.set(
//                                        null,
//                                        value.toBoolean()
//                                    )
//
//                                    GenericType.Types.FLOAT -> field.reference.set(
//                                        null,
//                                        value.toFloat()
//                                    )
//
//                                    GenericType.Types.LONG -> field.reference.set(
//                                        null,
//                                        value.toLong()
//                                    )
//
//                                    GenericType.Types.ENUM -> {
//                                        val enumValue =
//                                            field.reference.type.enumConstants.firstOrNull {
//                                                it.toString() == value
//                                            }
//                                        if (enumValue != null) {
//                                            field.reference.set(null, enumValue)
//                                        } else {
//                                            //
//                                        }
//                                    }
//
//                                    GenericType.Types.UNKNOWN -> TODO()
//                                    GenericType.Types.CUSTOM -> TODO()
//                                    GenericType.Types.ARRAY -> TODO()
//                                }
//
//                            }
                        }
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
