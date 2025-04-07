package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import lol.lazar.lazarkit.panels.Panels

@TeleOp(name = "Testing Telemetry", group = "Dashboard")
class TestingTelemetry : OpMode(
) {
    var panelsTelemetry = Panels.getTelemetry()
    var x = 0
    override fun init() {
        panelsTelemetry.debug("Hi, init was ran!")
        panelsTelemetry.update(telemetry)
        x = 0
    }

    override fun loop() {
        panelsTelemetry.debug("Value of x is $x")
        panelsTelemetry.update(telemetry)
        x++;
    }
}