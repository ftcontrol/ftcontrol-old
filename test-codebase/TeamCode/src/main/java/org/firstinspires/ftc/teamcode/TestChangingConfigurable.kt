package org.firstinspires.ftc.teamcode

import com.bylazar.ftcontrol.panels.Panels
import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@Configurable
@TeleOp(name = "TestChangingConfigurable")
class TestChangingConfigurable : OpMode() {
    companion object {
        @JvmField
        var timestamp = 0L
    }

    private val panelsTelemetry = Panels.getTelemetry()

    override fun init() {
    }

    override fun loop() {
        timestamp = System.currentTimeMillis()
        panelsTelemetry.debug("Timestamp: $timestamp")
        panelsTelemetry.update(telemetry)
    }
}