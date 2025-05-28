package org.firstinspires.ftc.teamcode.examples

import com.qualcomm.robotcore.hardware.HardwareMap

class ErrorProducer {
    lateinit var error: String

    lateinit var hardwareMap: HardwareMap

    fun init() {
        error = "init"
    }
}