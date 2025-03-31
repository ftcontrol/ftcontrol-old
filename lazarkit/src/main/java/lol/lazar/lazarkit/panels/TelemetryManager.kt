package lol.lazar.lazarkit.panels

import org.firstinspires.ftc.robotcore.external.Telemetry

class TelemetryManager(
    private val sendTelemetry: (lines: List<String>) -> Unit,
) {
    var lines = mutableListOf<String>()

    var lastUpdate = 0L
    var updateInterval = 500L
    val timeSinceLastUpdate: Long
        get() = System.currentTimeMillis() - lastUpdate
    val shouldUpdate: Boolean
        get() = timeSinceLastUpdate >= updateInterval

    fun debug(vararg data: String) {
        data.forEach { lines.add(it) }
    }

    fun update() {
        if (shouldUpdate) {
            sendTelemetry(lines)
            lastUpdate = System.currentTimeMillis()
        }
        lines.clear()
    }

    fun update(telemetry: Telemetry) {
        lines.forEach { telemetry.addLine(it) }
        telemetry.update()
        update()
    }
}