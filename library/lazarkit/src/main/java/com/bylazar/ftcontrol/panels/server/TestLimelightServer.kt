package com.bylazar.ftcontrol.panels.server

import android.content.Context
import android.content.res.AssetManager
import fi.iki.elonen.NanoHTTPD

class TestLimelightServer(var context: Context) : NanoHTTPD(3331) {
    private val assetManager: AssetManager = context.assets

    override fun serve(session: IHTTPSession): Response {

        if( session.uri == "/test") return newFixedLengthResponse("test")

        return newFixedLengthResponse("Hello, World!")
    }

    fun startServer() {
        start()
        println("DASH: LimelightServer started on port 3331")
    }

    fun stopServer() {
        stop()
        println("DASH: LimelightServer stopped")
    }
}