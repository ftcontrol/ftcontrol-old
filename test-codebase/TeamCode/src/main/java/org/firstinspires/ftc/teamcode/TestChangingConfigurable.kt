package org.firstinspires.ftc.teamcode

import com.bylazar.ftcontrol.panels.Panels
import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable
import com.pedropathing.follower.FollowerConstants
import com.pedropathing.paths.PathConstraints
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

class NullElement{
    var test = "lazar"
}

@Configurable
@TeleOp(name = "TestChangingConfigurable")
class TestChangingConfigurable : OpMode() {
    companion object {
        @JvmField
        var timestamp = 0L
        @JvmField
        var smallValue = 0.000001
        @JvmField
        var constants: FollowerConstants = FollowerConstants()
        @JvmField
        var constraints: PathConstraints = PathConstraints.defaultConstraints
        @JvmField
        var constraints2: PathConstraints = PathConstraints.defaultConstraints
        lateinit var nullElement: NullElement
    }

    private val panelsTelemetry = Panels.getTelemetry()

    override fun init() {
        nullElement = NullElement()
    }

    override fun loop() {
        timestamp = System.currentTimeMillis()
        panelsTelemetry.debug("Timestamp: $timestamp")
        panelsTelemetry.update(telemetry)
    }
}