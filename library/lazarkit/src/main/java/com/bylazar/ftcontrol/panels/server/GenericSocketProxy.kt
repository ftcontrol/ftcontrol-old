package com.bylazar.ftcontrol.panels.server

import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class GenericSocketProxy(
    val outsidePort: Int,
    val innerPort: Int,
    val innerIP: String
) {
    private var running = true

    fun startProxy() {
        thread {
            val serverSocket = ServerSocket(outsidePort)
            println("DASH: WebSocket Proxy started on port $outsidePort")

            while (running) {
                val clientSocket = serverSocket.accept()
                thread {
                    handleClient(clientSocket)
                }
            }
        }
    }

    fun stopProxy() {
        running = false
        // Optionally close the server socket cleanly
        println("DASH: WebSocket Proxy stopped")
    }

    private fun handleClient(clientSocket: Socket) {
        try {
            val serverSocket = Socket(innerIP, innerPort)

            val clientToServer = thread {
                pipe(clientSocket.getInputStream(), serverSocket.getOutputStream())
            }

            val serverToClient = thread {
                pipe(serverSocket.getInputStream(), clientSocket.getOutputStream())
            }

            clientToServer.join()
            serverToClient.join()

            clientSocket.close()
            serverSocket.close()
        } catch (e: Exception) {
            println("DASH: WebSocket proxy error: ${e.message}")
        }
    }

    private fun pipe(input: InputStream, output: OutputStream) {
        try {
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
                output.flush()
            }
        } catch (e: Exception) {
            // connection closed or error, do nothing
        }
    }
}
