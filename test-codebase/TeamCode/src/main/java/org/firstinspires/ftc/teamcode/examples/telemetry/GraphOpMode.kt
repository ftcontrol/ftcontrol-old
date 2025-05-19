package org.firstinspires.ftc.teamcode.examples.telemetry

import com.bylazar.ftcontrol.LoopTimer
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

    val timer = LoopTimer()

    var ticks = 0.0
    var wave = 0.0
    var wave2 = 0.0
    val constant = sin(0.0)

    val constant2 = sin(0.0) + 5

    override fun init() {
        panelsTelemetry.debug("Init was ran!")
        panelsTelemetry.update(telemetry)
        ticks = 0.0
        wave = 0.0
        wave2 = 0.0
    }

    override fun loop() {
        timer.start()
        ticks += ticksIncrement
        wave = sin(ticks)
        wave2 = sin(ticks + PI) * 2

        panelsTelemetry.debug("wave: $wave")
        panelsTelemetry.debug("wave2: $wave2")
        panelsTelemetry.debug("constant: $constant")
        panelsTelemetry.debug("constant2: $constant2")
        panelsTelemetry.graph("wave", wave)
        panelsTelemetry.graph("wave2", wave2)
        panelsTelemetry.graph("constant", constant)
        panelsTelemetry.graph("constant2", constant2)

        panelsTelemetry.debug("LoopTime: ${timer.ms}ms / ${timer.hz}Hz")

        panelsTelemetry.update(telemetry)
        timer.end()
    }
}