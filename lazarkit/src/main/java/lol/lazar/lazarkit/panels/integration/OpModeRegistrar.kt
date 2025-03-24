package lol.lazar.lazarkit.panels.integration

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta
import org.firstinspires.ftc.robotcore.internal.system.Misc
import lol.lazar.lazarkit.panels.Preferences

class OpModeRegistrar(
    private val toggle: () -> Unit,
) {
    fun registerOpMode(manager: OpModeManager) {
        manager.register(
            OpModeMeta.Builder()
                .setName("Enable/Disable Lazar's Dash")
                .setFlavor(OpModeMeta.Flavor.TELEOP)
                .setGroup("dash")
                .build(),
            object : LinearOpMode() {
                @Throws(InterruptedException::class)
                override fun runOpMode() {
                    telemetry.log().add(
                        Misc.formatInvariant(
                            "Dashboard is currently %s. Press Start to %s it.",
                            if (Preferences.isEnabled) "enabled" else "disabled",
                            if (Preferences.isEnabled) "disable" else "enable"
                        )
                    )
                    telemetry.update()

                    waitForStart()

                    if (isStopRequested) {
                        return
                    }

                    toggle()
                }
            })
    }
}