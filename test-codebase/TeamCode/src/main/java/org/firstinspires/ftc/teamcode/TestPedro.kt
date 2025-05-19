package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable
import com.bylazar.ftcontrol.panels.json.Look
import com.pedropathing.geometry.BezierCurve
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.Path
import com.pedropathing.util.Drawing
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@Configurable
@Config
@TeleOp(name="Test Pedro")
class TestPedro : OpMode() {
    companion object{
        @JvmField var pose = Pose(0.0, 0.0, 0.0)
    }
    override fun init() {

    }

    override fun loop() {
        Drawing.drawRobot(pose, Look("", "#3F51B5", 1.0, 0.0))
        Drawing.sendPacket()

        val path = Path(
            BezierCurve(
                Pose(0.0, 0.0),
                Pose(10.0, 0.0),
                Pose(24.0, 24.0)
            )
        )

        Drawing.drawPath(path, Look("", "#3F51B5", 1.0, 0.0))

        val packet = TelemetryPacket()
        packet.fieldOverlay().strokeCircle(pose.x ,pose.y, Drawing.ROBOT_RADIUS)
        FtcDashboard.getInstance().sendTelemetryPacket(packet)
    }
}