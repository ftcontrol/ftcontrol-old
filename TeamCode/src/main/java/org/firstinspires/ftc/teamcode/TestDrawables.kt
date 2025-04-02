package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import lol.lazar.lazarkit.panels.Panels
import lol.lazar.lazarkit.panels.data.Line
import lol.lazar.lazarkit.panels.data.Point
import lol.lazar.lazarkit.panels.configurables.annotations.Configurable
import lol.lazar.lazarkit.panels.data.Look

@Configurable
@TeleOp(name = "Testing Drawables OpMode", group = "Dashboard")
class TestDrawables : OpMode() {
    companion object {

    }

    var panelsTelemetry = Panels.getTelemetry()

    override fun init() {
        panelsTelemetry.debug("Init was ran!")
        panelsTelemetry.update(telemetry)
    }

    override fun loop() {
        panelsTelemetry.debug("Loop was ran!")
        panelsTelemetry.debug(
            Line(
                Point(0.0, 0.0),
                Point(0.0, 10.0),
                look = Look(
                    outlineColor = "red",
                    outlineWidth = 1.0,
                    fillColor = "",
                    opacity = 1.0
                )
            )
        )
        panelsTelemetry.update(telemetry)
    }
}