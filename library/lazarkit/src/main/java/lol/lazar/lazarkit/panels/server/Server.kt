package lol.lazar.lazarkit.panels.server

import android.content.Context
import android.content.res.AssetManager
import fi.iki.elonen.NanoHTTPD
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class Server(var context: Context) : NanoHTTPD(8001) {
    private val assetManager: AssetManager = context.assets
    private val client = OkHttpClient()

    init {
        println("DASH: ${assetManager.list("web")?.joinToString(", ")}")
        //TODO: files /data/data/com.qualcomm.ftcrobotcontroller
        val file = File(context.filesDir, "myfile.txt")
        file.writeText("Hello, world!")
    }

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri.removePrefix("/").removeSuffix("/").removePrefix("index.html").ifEmpty { "index.html" }

        if (uri.startsWith("limelight")) {
            return handleReverseProxy(session)
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

    private fun handleReverseProxy(session: IHTTPSession): Response {
        val targetUri = session.uri.removePrefix("/limelight")
        val url = "http://172.29.0.1:5801$targetUri"

        println("DASH: Proxying request to $url")

        try {
            val request = Request.Builder()
                .url(url)
                .method(session.method.name, null) // Forward the HTTP method
                .build()

            val response = client.newCall(request).execute()

            val responseBody = response.body?.byteStream()
            val contentType = response.header("Content-Type") ?: "application/octet-stream"

            return newChunkedResponse(
                Response.Status.lookup(response.code),
                contentType,
                responseBody
            )
        } catch (e: Exception) {
            val message = "DASH: Proxy error: ${e.message}"
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