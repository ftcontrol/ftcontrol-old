package lol.lazar.lazarkit.panels.server

import android.content.Context
import android.content.res.AssetManager
import fi.iki.elonen.NanoHTTPD
import okhttp3.OkHttpClient
import okhttp3.Request

class ServerLL(var context: Context) : NanoHTTPD(8003) {
    private val assetManager: AssetManager = context.assets
    private val client = OkHttpClient()


    override fun serve(session: IHTTPSession): Response {
        return handleReverseProxy(session)
    }

    private fun handleReverseProxy(session: IHTTPSession): Response {
        val url = "http://172.29.0.1:5801${session.uri}"

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
        println("DASH: Server started on port 8003")
    }

    fun stopServer() {
        stop()
        println("DASH: Server stopped")
    }
}