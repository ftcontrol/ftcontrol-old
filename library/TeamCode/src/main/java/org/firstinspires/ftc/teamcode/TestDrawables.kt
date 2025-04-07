package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import lol.lazar.lazarkit.LoopTimer
import lol.lazar.lazarkit.panels.Panels
import lol.lazar.lazarkit.panels.configurables.annotations.Configurable
import lol.lazar.lazarkit.panels.json.Circle
import lol.lazar.lazarkit.panels.json.Line
import lol.lazar.lazarkit.panels.json.Look
import lol.lazar.lazarkit.panels.json.Point
import lol.lazar.lazarkit.panels.json.Rectangle
import kotlin.random.Random

@Configurable
@TeleOp(name = "Testing Drawables OpMode", group = "Dashboard")
class TestDrawables : OpMode() {
    var timer = LoopTimer()

    var panelsTelemetry = Panels.getTelemetry()

    override fun init() {
        panelsTelemetry.debug("Init was ran!")
        panelsTelemetry.update(telemetry)
    }

    override fun loop() {
        timer.start()
        panelsTelemetry.debug("Loop was ran!")
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
        panelsTelemetry.debug("LoopTime: ${timer.hz}")
        println("DASH: LoopTime: ${timer.hz} / startTime: ${timer.startTime} / endTime: ${timer.endTime}")
        panelsTelemetry.update(telemetry)
        timer.end()
    }
}