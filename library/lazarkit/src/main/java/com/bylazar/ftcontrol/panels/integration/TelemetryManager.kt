package com.bylazar.ftcontrol.panels.integration

import com.bylazar.ftcontrol.panels.json.Canvas
import com.bylazar.ftcontrol.panels.json.CanvasPresets
import com.bylazar.ftcontrol.panels.json.CanvasRotation
import com.bylazar.ftcontrol.panels.json.Drawable
import com.bylazar.ftcontrol.panels.json.GraphPacket
import org.firstinspires.ftc.robotcore.external.Telemetry

class TelemetryManager(
    private val sendLinesSocket: (lines: MutableList<String>) -> Unit,
    private val sendGraphSocket: (graph: MutableMap<String, MutableList<GraphPacket>>) -> Unit,
    private val sendCanvasSocket: (canvas: Canvas) -> Unit,
) {
    var lines = mutableListOf<String>()
    var canvas = Canvas()
    var graph: MutableMap<String, MutableList<GraphPacket>> = mutableMapOf()

    var lastZIndex = 0

    var graphUpdates: MutableMap<String, Long> = mutableMapOf()


    var linesUpdateInterval = 50L
    var lastLinesUpdate = 0L
    val timeSinceLastLinesUpdate: Long
        get() = System.currentTimeMillis() - lastLinesUpdate
    val shouldUpdateLines: Boolean
        get() = timeSinceLastLinesUpdate >= linesUpdateInterval

    var graphUpdateInterval = 10L
    var lastGraphUpdate = 0L
    val timeSinceLastGraphUpdate: Long
        get() = System.currentTimeMillis() - lastGraphUpdate
    val shouldUpdateGraph: Boolean
        get() = timeSinceLastGraphUpdate >= graphUpdateInterval

    var canvasUpdateInterval = 50L
    var lastCanvasUpdate = 0L
    val timeSinceLastCanvasUpdate: Long
        get() = System.currentTimeMillis() - lastCanvasUpdate
    val shouldUpdateCanvas: Boolean
        get() = timeSinceLastCanvasUpdate >= canvasUpdateInterval

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
                is Canvas -> {
                    it.lines.forEach { line ->
                        line.zIndex = lastZIndex + if (line.zIndex > 0) line.zIndex else 0
                        lastZIndex++
                        canvas.add(line)
                    }
                    it.rectangles.forEach { rectangle ->
                        rectangle.zIndex = lastZIndex + if (rectangle.zIndex > 0) rectangle.zIndex else 0
                        lastZIndex++
                        canvas.add(rectangle)
                    }
                    it.circles.forEach { circle ->
                        circle.zIndex = lastZIndex + if (circle.zIndex > 0) circle.zIndex else 0
                        lastZIndex++
                        canvas.add(circle)
                    }
                }

                else -> {}
            }
        }
    }

    fun sendCanvas(c: Canvas){
        if(c.isEmpty) return
        if (shouldUpdateCanvas){
            sendCanvasSocket(c)
            lastCanvasUpdate = System.currentTimeMillis()
        }
    }

    fun sendGraph(g: MutableMap<String, MutableList<GraphPacket>>) {
        if (g.isEmpty()) return
        if (shouldUpdateGraph) {
            sendGraphSocket(g)
            lastGraphUpdate = System.currentTimeMillis()
        }
    }

    fun sendLines(l: MutableList<String>) {
        if (l.isEmpty()) return
        if (shouldUpdateLines) {
            sendLinesSocket(l)
            lastLinesUpdate = System.currentTimeMillis()
        }
    }

    fun update() {
        sendLines(lines)
        sendGraph(graph)
        sendCanvas(canvas)
        lastZIndex = 0
        lines.clear()
        canvas.clear()
    }

    fun update(telemetry: Telemetry) {
        lines.forEach { telemetry.addLine(it) }
        telemetry.update()
        update()
    }

    fun setOffsets(offsetX: Double, offsetY: Double, rotation: CanvasRotation = CanvasRotation.DEG_0){
        canvas = canvas.withOffsets(offsetX, offsetY, rotation)
    }
    fun setOffsetX(offsetX: Double){
        canvas = canvas.withOffsetX(offsetX)
    }
    fun setOffsetY(offsetY: Double){
        canvas = canvas.withOffsetY(offsetY)
    }
    fun setRotation(rotation: CanvasRotation){
        canvas = canvas.withRotation(rotation)
    }
    fun setOffsetPreset(preset: CanvasPresets){
        canvas = canvas.withOffsetPreset(preset)
    }
}