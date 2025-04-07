package lol.lazar.lazarkit.panels.server

import android.content.Context
import fi.iki.elonen.NanoHTTPD
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayInputStream

class LimelightProxy(private val context: Context) : NanoHTTPD(8003) {

//    val llURL = "172.29.0.1"
//    val llPort = 5801

    val llURL = "localhost"
    val llPort = 8001

    private val client = OkHttpClient.Builder().build()

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
            .url("http://$llURL:$llPort${session.uri}")
            .method(session.method.name, getRequestBody(session))
            .apply {
                session.headers
                    .filterKeys { it.lowercase() != "host" }
                    .forEach { (key, value) -> addHeader(key, value) }
            }
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.bytes() ?: ByteArray(0)

        val mimeType = response.header("Content-Type") ?: "application/octet-stream"
        val encoding = response.header("Content-Encoding")

        val resp = newFixedLengthResponse(
            Response.Status.lookup(response.code) ?: Response.Status.INTERNAL_ERROR,
            mimeType,
            ByteArrayInputStream(responseBody),
            responseBody.size.toLong()
        )

        encoding?.let {
            resp.addHeader("Content-Encoding", it)
        }

        listOf("Cache-Control", "Content-Language", "ETag").forEach { header ->
            response.header(header)?.let { resp.addHeader(header, it) }
        }

        return resp
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