package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import lol.lazar.lazarkit.panels.Panels
import lol.lazar.lazarkit.panels.configurables.annotations.Configurable
import kotlin.jvm.JvmField

@Configurable
@Autonomous(name = "Testing Auto OpMode", group = "Dashboard")
class TestingAuto : OpMode(
) {
    companion object {
        @JvmField
        var number = 0

        @JvmField
        var testArray = intArrayOf(1, 2, 3)
    }

    var panelsTelemetry = Panels.getTelemetry()

    override fun init() {
        panelsTelemetry.debug("Hi, init was ran!")
        panelsTelemetry.update(telemetry)
    }

    override fun start() {
    }

    override fun loop() {
        panelsTelemetry.debug("Number is $number")

        panelsTelemetry.debug("Array is ${testArray.joinToString()}")

        panelsTelemetry.update(telemetry)
    }
}