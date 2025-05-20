package org.firstinspires.ftc.teamcode.examples.field

import com.bylazar.ftcontrol.panels.Panels
import com.bylazar.ftcontrol.panels.json.CanvasRotation
import com.bylazar.ftcontrol.panels.json.Circle
import com.bylazar.ftcontrol.panels.json.Line
import com.bylazar.ftcontrol.panels.json.Look
import com.bylazar.ftcontrol.panels.json.Point
import com.bylazar.ftcontrol.panels.json.Rectangle
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlin.random.Random

@TeleOp(name = "Field Test OpMode")
class TestOpMode : OpMode() {
    var panelsTelemetry = Panels.getTelemetry()

    override fun init() {
        panelsTelemetry.setOffsets(24.0, 24.0, CanvasRotation.DEG_0) //field offsets in inches
        panelsTelemetry.debug("Init was ran!")
        panelsTelemetry.update(telemetry)
    }

    override fun loop() {
        panelsTelemetry.debug("Loop ${System.currentTimeMillis()} ran!")
        panelsTelemetry.debug(
            Line(
                Point(0.0, 0.0),
                Point(0.0, Random.nextDouble(0.0, 10.0)),
            ).withLook(
                Look(
                    outlineColor = "red",
                    outlineWidth = 1.0,
                    fillColor = "",
                    opacity = 1.0
                )
            ),
            Circle(
                Point(20.0, 20.0),
                Random.nextDouble(0.0, 10.0),
            ).withLook(
                Look(
                    outlineColor = "blue",
                    outlineWidth = 1.0,
                    fillColor = "yellow",
                    opacity = 1.0
                )
            ),
            Rectangle(
                Point(40.0, 40.0),
                Random.nextDouble(0.0, 10.0),
                Random.nextDouble(0.0, 10.0),
            ).withLook(
                Look(
                    outlineColor = "green",
                    outlineWidth = 1.0,
                    fillColor = "",
                    opacity = 1.0
                )
            )
        )
        panelsTelemetry.update(telemetry)
    }
}