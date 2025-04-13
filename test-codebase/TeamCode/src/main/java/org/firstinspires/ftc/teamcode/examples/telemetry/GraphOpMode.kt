package org.firstinspires.ftc.teamcode.examples.telemetry

import com.bylazar.ftcontrol.panels.Panels
import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable
import com.bylazar.ftcontrol.panels.integration.TelemetryManager
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlin.math.sin

@Configurable
@TeleOp(name = "Test Telemetry Graph OpMode")
class GraphOpMode : OpMode() {
    companion object{
        @JvmField var ticksIncrement = 0.025
    }
    private val panelsTelemetry: TelemetryManager = Panels.getTelemetry()

    var ticks = 0.0
    var value = 0.0

    override fun init() {
        panelsTelemetry.debug("Init was ran!")
        panelsTelemetry.update(telemetry)
        ticks = 0.0
        value = 0.0
    }

    override fun loop() {
        ticks += ticksIncrement
        value = sin(ticks)

        panelsTelemetry.debug("Here is a variable: $value")
        panelsTelemetry.graph("wave", value)
        panelsTelemetry.update(telemetry)
    }
}