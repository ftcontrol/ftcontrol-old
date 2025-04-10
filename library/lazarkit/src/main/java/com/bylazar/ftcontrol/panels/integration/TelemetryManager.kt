package com.bylazar.ftcontrol.panels.integration

import com.bylazar.ftcontrol.panels.json.Canvas
import com.bylazar.ftcontrol.panels.json.Drawable
import org.firstinspires.ftc.robotcore.external.Telemetry

class TelemetryManager(
    private val sendTelemetry: (lines: List<String>, canvas: Canvas) -> Unit,
) {
    var lines = mutableListOf<String>()
    var canvas = Canvas()

    var lastZIndex = 0

    var lastUpdate = 0L
    var updateInterval = 200L
    val timeSinceLastUpdate: Long
        get() = System.currentTimeMillis() - lastUpdate
    val shouldUpdate: Boolean
        get() = timeSinceLastUpdate >= updateInterval

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
            sendTelemetry(lines, canvas)
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