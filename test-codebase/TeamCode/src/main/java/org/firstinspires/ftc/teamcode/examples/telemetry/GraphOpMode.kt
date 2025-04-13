package org.firstinspires.ftc.teamcode.examples.telemetry

import com.bylazar.ftcontrol.panels.Panels
import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable
import com.bylazar.ftcontrol.panels.integration.TelemetryManager
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlin.math.PI
import kotlin.math.sin

@Configurable
@TeleOp(name = "Test Telemetry Graph OpMode")
class GraphOpMode : OpMode() {
    companion object{
        @JvmField var ticksIncrement = 0.025
    }
    private val panelsTelemetry: TelemetryManager = Panels.getTelemetry()

    var ticks = 0.0
    var wave = 0.0
    var wave2 = 0.0
    val constant = sin(0.0)

    override fun init() {
        panelsTelemetry.debug("Init was ran!")
        panelsTelemetry.update(telemetry)
        ticks = 0.0
        wave = 0.0
        wave2 = 0.0
    }

    override fun loop() {
        ticks += ticksIncrement
        wave = sin(ticks)
        wave2 = sin(ticks + PI)

        panelsTelemetry.debug("wave: $wave")
        panelsTelemetry.debug("wave2: $wave2")
        panelsTelemetry.debug("constant: $constant")
        panelsTelemetry.graph("wave", wave)
        panelsTelemetry.graph("wave2", wave2)
        panelsTelemetry.graph("constant", constant)
        panelsTelemetry.update(telemetry)
    }
}