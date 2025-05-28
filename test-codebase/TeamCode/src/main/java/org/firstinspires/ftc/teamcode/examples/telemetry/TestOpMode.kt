package org.firstinspires.ftc.teamcode.examples.telemetry

import com.bylazar.ftcontrol.panels.Panels
import com.bylazar.ftcontrol.panels.integration.TelemetryManager
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Test Telemetry OpMode")
class TestOpMode : OpMode() {
    private val panelsTelemetry: TelemetryManager = Panels.getTelemetry()

    override fun init() {
        panelsTelemetry.debug("Init was ran!")
        panelsTelemetry.update(telemetry)
    }

    override fun loop() {
        panelsTelemetry.debug(
            "This is a line!",
            "This is another line."
        )
        panelsTelemetry.debug("Here is a variable: ${Math.PI}")
        panelsTelemetry.update(telemetry)
    }
}