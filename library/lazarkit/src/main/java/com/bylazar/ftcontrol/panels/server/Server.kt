package com.bylazar.ftcontrol.panels.server

import android.content.Context
import android.content.res.AssetManager
import com.bylazar.ftcontrol.panels.plugins.PluginManager
import fi.iki.elonen.NanoHTTPD
import java.io.File

class Server(var context: Context) : NanoHTTPD(8001) {
    private val assetManager: AssetManager = context.assets

    init {
        //TODO: files /data/data/com.qualcomm.ftcrobotcontroller
        val file = File(context.filesDir, "myfile.txt")
        file.writeText("Hello, world!")
    }

    fun allowCors(response: Response): Response {
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "*")
        return response
    }

    fun getResponse(
        content: String,
        contentType: String = "text/html",
        status: Response.Status = Response.Status.OK
    ): Response {
        return allowCors(
            newFixedLengthResponse(
                status,
                contentType,
                content
            )
        )
    }


    override fun serve(session: IHTTPSession): Response {
        if (session.method == Method.OPTIONS) {
            return getResponse("")
        }
        val uri = session.uri.removePrefix("/").removeSuffix("/").removePrefix("index.html")
            .ifEmpty { "index.html" }
        val parts = uri.split("/")
        if (uri.startsWith("plugins") && parts.size > 2) {
            val pluginId = parts[1]
            val endpoint = parts[2]
            when (endpoint) {
                "details" -> {
                    val jsonString =
                        """{"path": "$uri", "id": "$pluginId", "name": "${PluginManager.plugins.values.find { it.id == pluginId }?.name ?: "Unknown"}"}"""

                    return getResponse(jsonString, contentType = "application/json")
                }

                "html" -> {
                    val htmlString = """
                        <h1>Plugin Details</h1>
                        <p>Path: $uri</p>
                        <p>ID: $pluginId</p>
                        <p>Name: ${PluginManager.plugins.values.find { it.id == pluginId }?.name ?: "Unknown"}</p>
                        <p style="color: var(--primary);">Timestamp: ${System.currentTimeMillis()}</p>                           
                    """.trimIndent()

                    return getResponse(htmlString)
                }
            }
        }

        return getStaticResponse(uri)
    }

    private fun getStaticResponse(uri: String): Response {
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

        try {
            val inputStream = assetManager.open(path)
            println("DASH: success")
            return allowCors(newChunkedResponse(Response.Status.OK, mime, inputStream))
        } catch (e: Exception) {
            println("DASH: Primary asset not found: $path — ${e.message}")
            e.printStackTrace()

            return try {
                val fallbackStream = assetManager.open("web/index.html")
                println("DASH: Fallback to index.html")
                allowCors(newChunkedResponse(Response.Status.OK, "text/html", fallbackStream))
            } catch (fallbackException: Exception) {
                val message = "DASH: Fallback also failed: ${fallbackException.message}"
                println(message)
                fallbackException.printStackTrace()
                getResponse(message, status = Response.Status.INTERNAL_ERROR)
            }
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