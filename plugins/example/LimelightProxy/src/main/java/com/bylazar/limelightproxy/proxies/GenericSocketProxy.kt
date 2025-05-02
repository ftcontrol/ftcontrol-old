package com.bylazar.limelightproxy.proxies

import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class GenericSocketProxy(
    private val outsidePort: Int,
    private val innerPort: Int,
    private val innerIP: String
) {
    @Volatile
    private var running = false

    private var serverThread: Thread? = null
    private var serverSocket: ServerSocket? = null

    fun startProxy() {
        if (running) return
        running = true
        serverThread = thread(name = "SocketProxyThread") {
            try {
                serverSocket = ServerSocket(outsidePort)
                println("DASH: WebSocket Proxy started on port $outsidePort")

                while (running) {
                    try {
                        val clientSocket = serverSocket?.accept() ?: break
                        thread {
                            handleClient(clientSocket)
                        }
                    } catch (e: Exception) {
                        if (running) println("DASH: Error accepting client: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("DASH: Failed to start proxy: ${e.message}")
            } finally {
                serverSocket?.close()
                println("DASH: Server socket closed")
            }
        }
    }

    fun stopProxy() {
        if (!running) return
        running = false
        try {
            serverSocket?.close()
        } catch (e: Exception) {
            println("DASH: Error closing server socket: ${e.message}")
        }
        serverThread?.join()
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
        } catch (_: Exception) {
            // connection closed or error
        }
    }
}
