package com.bylazar.ftcontrol.panels.integration

import com.bylazar.ftcontrol.panels.json.Canvas
import com.bylazar.ftcontrol.panels.json.Drawable
import com.bylazar.ftcontrol.panels.json.GraphPacket
import org.firstinspires.ftc.robotcore.external.Telemetry

class TelemetryManager(
    private val sendTelemetry: (lines: List<String>, canvas: Canvas, graph: MutableMap<String, MutableList<GraphPacket>>) -> Unit,
) {
    var lines = mutableListOf<String>()
    var canvas = Canvas()
    var graph: MutableMap<String, MutableList<GraphPacket>> = mutableMapOf()

    var lastZIndex = 0

    var lastUpdate = 0L
    var updateInterval = 50L
    var graphUpdateInterval = 5L
    var graphUpdates: MutableMap<String, Long> = mutableMapOf()
    val timeSinceLastUpdate: Long
        get() = System.currentTimeMillis() - lastUpdate
    val shouldUpdate: Boolean
        get() = timeSinceLastUpdate >= updateInterval

    fun resetGraphs(){
        graph.clear()
        graphUpdates.clear()
    }

    private fun graph(key: String, data: String) {
        if(graphUpdates[key] == null) graphUpdates[key] = 0
        if(System.currentTimeMillis() - graphUpdates[key]!! < graphUpdateInterval) return
        val currentTime = System.currentTimeMillis()
        val oneMinuteAgo = currentTime - 60_000

        if (!graph.containsKey(key)) {
            graph[key] = mutableListOf()
        }

        val list = graph[key]!!

        list.removeIf { it.timestamp < oneMinuteAgo }

        list.add(GraphPacket(data, currentTime))
        graphUpdates[key] = System.currentTimeMillis()
    }

    fun graph(key: String, data: Double) = graph(key, data.toString())
    fun graph(key: String, data: Int) = graph(key, data.toString())
    fun graph(key: String, data: Float) = graph(key, data.toString())
    fun graph(key: String, data: Long) = graph(key, data.toString())


    fun debug(vararg data: String) {
        data.forEach { lines.add(it) }
    }

    fun debug(vararg data: Any) {
        data.forEach {
            when (it) {
                is String -> lines.add(it)
                is Drawable -> {
                    it.zIndex = lastZIndex + if (it.zIndex > 0) it.zIndex else 0
                    lastZIndex++
                    canvas.add(it)
                }

                else -> {}
            }
        }
    }

    fun update() {
        if (shouldUpdate) {
            sendTelemetry(lines, canvas, graph)
            lastUpdate = System.currentTimeMillis()
        }
        lastZIndex = 0
        lines.clear()
        canvas.clear()
    }

    fun update(telemetry: Telemetry) {
        lines.forEach { telemetry.addLine(it) }
        telemetry.update()
        update()
    }
}