package com.bylazar.limelightproxy

import fi.iki.elonen.NanoHTTPD

class TestLimelightServer() : NanoHTTPD(3331) {
    override fun serve(session: IHTTPSession): Response {

        if (session.uri == "/test") return newFixedLengthResponse("test")

        return newFixedLengthResponse("Hello, World!")
    }

    fun startServer() {
        if(isAlive) return
        start()
        println("DASH: LimelightServer started on port 3331")
    }

    fun stopServer() {
        if(!isAlive) return
        stop()
        println("DASH: LimelightServer stopped")
    }
}