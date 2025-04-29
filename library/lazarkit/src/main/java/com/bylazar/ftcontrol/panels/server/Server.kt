package com.bylazar.ftcontrol.panels.server

import android.content.Context
import android.content.res.AssetManager
import com.bylazar.ftcontrol.panels.plugins.PluginManager
import fi.iki.elonen.NanoHTTPD
import okhttp3.OkHttpClient
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

        if (uri.startsWith("plugins")) {
            val parts = uri.split("/")
            val pluginId = if (parts.size > 1) parts[1] else "unknown"

            if (pluginId == "unknown") return getResponse(
                "Plugin not found",
                status = Response.Status.NOT_FOUND
            )

            val endpoint = if (parts.size > 2) parts[2] else "details"

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

                else -> return getResponse(
                    "Endpoint not found",
                    status = Response.Status.NOT_FOUND,
                )
            }
        }

        return getStaticResponse(uri)
    }

    private fun getStaticResponse(uri: String): NanoHTTPD.Response {
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
            val message = "DASH: Server error: ${e.message}"
            println(message)
            e.printStackTrace()
            return getResponse(message, status = Response.Status.INTERNAL_ERROR)
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