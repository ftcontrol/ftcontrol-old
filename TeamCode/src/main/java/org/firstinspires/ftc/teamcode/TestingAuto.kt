package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import lol.lazar.lazarkit.panels.Panels
import lol.lazar.lazarkit.panels.configurables.annotations.Configurable

@Configurable
@Autonomous(name = "Testing Auto OpMode", group = "Dashboard")
class TestingAuto : OpMode(
) {
    companion object {
        @JvmField
        var intValue: Int = 0

        @JvmField
        var longValue: Long = 0L

        @JvmField
        var doubleValue: Double = 0.0

        @JvmField
        var floatValue: Float = 0.0f

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
        panelsTelemetry.debug("Hi, loop was ran!")
        panelsTelemetry.debug("Int is $intValue")
        panelsTelemetry.debug("Long is $longValue")
        panelsTelemetry.debug("Double is $doubleValue")
        panelsTelemetry.debug("Float is $floatValue")

        panelsTelemetry.debug("Array is ${testArray.joinToString()}")

        panelsTelemetry.update(telemetry)
    }
}