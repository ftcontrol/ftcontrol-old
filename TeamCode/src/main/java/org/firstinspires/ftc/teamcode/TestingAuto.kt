package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@Autonomous(name = "Testing Auto OpMode", group = "Dashboard")
class TestingAuto : OpMode(
) {
    var lastTime = System.currentTimeMillis()
    override fun init() {
    }

    override fun start() {
        lastTime = System.currentTimeMillis()
    }

    override fun loop() {
        telemetry.addLine("Hi!")
        telemetry.update()
        println("DASH: Auto OpMode${System.currentTimeMillis() - lastTime}")
    }
}