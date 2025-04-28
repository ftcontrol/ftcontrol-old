package com.bylazar.ftcontrol.panels.server

import android.content.Context
import android.content.res.AssetManager
import com.bylazar.ftcontrol.panels.plugins.PluginManager
import fi.iki.elonen.NanoHTTPD
import okhttp3.OkHttpClient
import java.io.File

class Server(var context: Context) : NanoHTTPD(8001) {
    private val assetManager: AssetManager = context.assets
    private val client = OkHttpClient()

    init {
        //TODO: files /data/data/com.qualcomm.ftcrobotcontroller
        val file = File(context.filesDir, "myfile.txt")
        file.writeText("Hello, world!")
    }

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri.removePrefix("/").removeSuffix("/").removePrefix("index.html")
            .ifEmpty { "index.html" }

        if (uri.startsWith("plugins")) {
            val parts = uri.split("/")
            val pluginId = if (parts.size > 1) parts[1] else "unknown"

            if (pluginId == "unknown") return newFixedLengthResponse(
                Response.Status.NOT_FOUND,
                "text/plain",
                "Plugin not found"
            )

            val endpoint = if (parts.size > 2) parts[2] else "details"

            when (endpoint) {
                "details" -> {
                    val jsonString =
                        """{"path": "$uri", "id": "$pluginId", "name": "${PluginManager.plugins.values.find { it.id == pluginId }?.name ?: "Unknown"}"}"""

                    return newFixedLengthResponse(Response.Status.OK, "application/json", jsonString)
                }
                "html" -> {
                    return newFixedLengthResponse(Response.Status.OK, "text/html", """
                        <html>
                            <head>
                                <title>Plugin Details</title>
                            </head>
                            <body>
                                <h1>Plugin Details</h1>
                                <p>Path: $uri</p>
                                <p>ID: $pluginId</p>
                                <p>Name: ${PluginManager.plugins.values.find { it.id == pluginId }?.name ?: "Unknown"}</p>
                                <p>Timestamp: ${System.currentTimeMillis()}</p>                           
                            </body>
                        </html>
                    """.trimIndent())
                }
                else -> return newFixedLengthResponse(
                    Response.Status.NOT_FOUND,
                    "text/plain",
                    "Endpoint not found"
                )
            }
        }

        val path = when {
            !uri.contains(".") -> "web/$uri/index.html"
            else -> "web/$uri"
        }

        val mime = when (path.substringAfterLast(".", "")) {
            "css" -> "text/css"
            "htm", "html" -> "text/html"
            "js" -> "application/javascript"
            else -> "application/octet-stream"
        }

        println("DASH: Request for ${session.uri} / $uri / $path / $mime")


        try {
            val inputStream = assetManager.open(path)
            println("DASH: success")
            return newChunkedResponse(Response.Status.OK, mime, inputStream)
        } catch (e: Exception) {
            val message = "DASH: Server error: ${e.message}"
            println(message)
            e.printStackTrace()
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", message)
        }
    }

    fun startServer() {
        start()
        println("DASH: Server started on port 8001")
    }

    fun stopServer() {
        stop()
        println("DASH: Server stopped")
    }
}