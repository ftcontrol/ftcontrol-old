package org.firstinspires.ftc.teamcode.examples;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Config
@Configurable
@TeleOp(name = "Test Init")
public class TestInit extends OpMode {
    public static String hello = "test";
    public static ExampleFollower robot;

    @Override
    public void init() {
        robot = new ExampleFollower();
        robot.init();

    }

    @Override
    public void loop() {
        System.out.println(robot);
    }
}
