package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import lol.lazar.lazarkit.panels.DashboardRegistrar

@TeleOp(name = "Testing OpMode", group = "Dashboard")
class TestingOpMode : OpMode(
) {
    override fun init() {
        DashboardRegistrar.getInstance().send()
    }

    override fun loop() {
        telemetry.addLine("Hi!")
//        DashboardRegistrar.getInstance().send()
//        if(gamepad1.triangle){
//            telemetry.addLine("Triangle pressed")
//            println("DASH: Triangle pressed")
//        }

        telemetry.update()
    }
}