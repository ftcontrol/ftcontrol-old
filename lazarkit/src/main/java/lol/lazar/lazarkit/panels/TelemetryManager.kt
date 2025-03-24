package lol.lazar.lazarkit.panels

import org.firstinspires.ftc.robotcore.external.Telemetry

class TelemetryManager(
    private val sendTelemetry: (lines: List<String>) -> Unit,
) {
    var lines = mutableListOf<String>()

    fun debug(vararg data: String) {
        data.forEach { lines.add(it) }
    }

    fun update() {
        sendTelemetry(lines)
        lines.clear()
    }

    fun update(telemetry: Telemetry) {
        lines.forEach { telemetry.addLine(it) }
        telemetry.update()
        update()
    }
}