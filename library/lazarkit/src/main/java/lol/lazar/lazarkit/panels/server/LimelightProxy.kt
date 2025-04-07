package lol.lazar.lazarkit.panels.server

import android.content.Context
import fi.iki.elonen.NanoHTTPD
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayInputStream
import java.net.InetSocketAddress
import java.net.Proxy

class LimelightProxy(private val context: Context) : NanoHTTPD(8003) {

    private val client = OkHttpClient.Builder()
        .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("172.29.0.1", 5801)))
        .build()

    override fun serve(session: IHTTPSession): Response {
        return try {
            handleReverseProxy(session)
        } catch (e: Exception) {
            println("DASH: Proxy error: ${e.message}")
            e.printStackTrace()
            newFixedLengthResponse(
                Response.Status.INTERNAL_ERROR,
                "text/plain",
                "Internal Server Error"
            )
        }
    }

    private fun handleReverseProxy(session: IHTTPSession): Response {
        println("DASH: Proxying request to ${session.uri}")

        val request = Request.Builder()
            .url("http://172.29.0.1:5801${session.uri}")
            .method(session.method.name, getRequestBody(session))
            .apply {
                session.headers
                    .filterKeys { it.lowercase() != "host" }
                    .forEach { (key, value) -> addHeader(key, value) }
            }
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.bytes() ?: ByteArray(0)

        return newFixedLengthResponse(
            Response.Status.lookup(response.code) ?: Response.Status.INTERNAL_ERROR,
            response.header("Content-Type") ?: "application/octet-stream",
            ByteArrayInputStream(responseBody),
            responseBody.size.toLong()
        )
    }

    private fun getRequestBody(session: IHTTPSession): RequestBody? {
        if (session.method in listOf(Method.POST, Method.PUT, Method.PATCH)) {
            val buffer = session.inputStream.readBytes()
            val mediaType = session.headers["content-type"]?.toMediaTypeOrNull()
            return buffer.toRequestBody(mediaType)
        }
        return null
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