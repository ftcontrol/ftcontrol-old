package org.lazarkit.panels.server

import fi.iki.elonen.NanoWSD
import org.lazarkit.panels.GlobalData
import org.lazarkit.panels.GlobalGamepad
import org.lazarkit.panels.OpModeData
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

    fun broadcastActiveOpMode() {
        for (client in clients) {
            try {
                client.updateCurrentOpMode()
            } catch (e: IOException) {
                println("DASH: Error sending message to client: ${e.message}")
            }
        }
    }

    fun sendTest() {
        println("DASH: sent test")
        for (client in clients) {
            try {
                client.send("test.info")
            } catch (e: IOException) {
                println("DASH: Error sending message to client: ${e.message}")
            }
        }
    }

    private inner class TimeWebSocket(handshake: IHTTPSession) : WebSocket(handshake) {
        private var timer: Timer = Timer()

        private var ping: TimerTask? = null

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
        }

        fun startSendingTime() {
            timer.schedule(object : TimerTask() {
                override fun run() {
                    try {
                        val time = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(Date())
                        send("time.$time")
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

        fun updateCurrentOpMode() {
            val status = when (GlobalData.status) {
                OpModeData.OpModeStatus.INIT -> "init"
                OpModeData.OpModeStatus.RUNNING -> "running"
                OpModeData.OpModeStatus.STOPPED -> "stopped"
            }
            send("current_opmode.${GlobalData.activeOpModeName}.$status")
        }

        fun setTriangle(value: Boolean) {
            val currentOpMode = GlobalData.activeOpMode
            if (currentOpMode == null) {
                println("DASH: No active opmode")
                return
            }

            println("DASH: Triangle (before): ${GlobalGamepad.dpad_up} / $value / ${currentOpMode.time}")

            currentOpMode.gamepad1?.dpad_up = value
            GlobalGamepad.dpad_up = value
            currentOpMode.gamepad1?.refreshTimestamp()

            println("DASH: Triangle (after): ${GlobalGamepad.dpad_up} / $value / ${currentOpMode.time}")
        }

        override fun onMessage(message: WebSocketFrame) {
            try {
                println("DASH: Received message: ${message.textPayload}")
                val parts = message.textPayload.split(".")
                if (parts.isEmpty()) {
                    println("DASH: Ignoring malformed message: ${message.textPayload}")
                    return
                }
                val type = parts[0]
                val data = parts.subList(1, parts.size)

                when (type) {
                    "get_opmodes" -> {
                        send("opmodes.${GlobalData.opModeListString}")
                    }

                    "get_current_opmode" -> {
                        updateCurrentOpMode()
                    }

                    "init_opmode" -> {
                        initOpMode(data[0])
                    }

                    "start_opmode" -> {
                        startOpMode()
                    }

                    "stop_opmode" -> {
                        stopOpMode()
                    }

                    "triangle" -> {
                        when (data[0]) {
                            "1" -> setTriangle(true)
                            "0" -> setTriangle(false)
                        }
                    }

                    else -> println("DASH: Unknown command $type")
                }
            } catch (e: Exception) {
                println("DASH: Error processing WebSocket message: ${e.message}")
                e.printStackTrace()
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
