package com.bylazar.ftcontrol.panels.server

import android.content.Context
import android.content.res.AssetManager
import com.bylazar.ftcontrol.panels.Logger
import com.bylazar.ftcontrol.panels.plugins.PluginManager
import fi.iki.elonen.NanoHTTPD
import java.io.File

class Server(var context: Context) : NanoHTTPD(8001) {
    private val assetManager: AssetManager = context.assets

    init {
        //TODO: files /data/data/com.qualcomm.ftcrobotcontroller
        val file = File(context.filesDir, "myfile.txt")
        file.writeText("Hello, world!")

        var files = listWebFiles("web")

        files.forEach {
            Logger.serverLog("Found file: $it")
        }

        files = listWebFiles("web2")

        files.forEach {
            Logger.serverLog("Found file2: $it")
        }
    }

    fun listWebFiles(path: String = "web"): List<String> {
        val fileList = mutableListOf<String>()

        fun listFilesRecursive(currentPath: String) {
            try {
                val files = assetManager.list(currentPath) ?: return
                for (file in files) {
                    val fullPath = if (currentPath.isEmpty()) file else "$currentPath/$file"
                    val subFiles = assetManager.list(fullPath)
                    if (subFiles == null || subFiles.isEmpty()) {
                        fileList.add(fullPath)
                    } else {
                        listFilesRecursive(fullPath)
                    }
                }
            } catch (e: Exception) {
                Logger.serverLog("Error listing files at $currentPath: ${e.message}")
            }
        }

        listFilesRecursive(path)
        return fileList
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
            Logger.serverLog("Success")
            return allowCors(newChunkedResponse(Response.Status.OK, mime, inputStream))
        } catch (e: Exception) {
            Logger.serverLog("Primary asset not found: $path — ${e.message}")

            return try {
                val fallbackStream = assetManager.open("web/index.html")
                Logger.serverLog("Fallback to index.html")
                allowCors(newChunkedResponse(Response.Status.OK, "text/html", fallbackStream))
            } catch (fallbackException: Exception) {
                val message = "Fallback also failed: ${fallbackException.message}"
                Logger.serverLog(message)
                getResponse(message, status = Response.Status.INTERNAL_ERROR)
            }
        }
    }


    fun startServer() {
        start()
        Logger.serverLog("Server started on port 8001")
    }

    fun stopServer() {
        stop()
        Logger.serverLog("Server stopped")
    }
}