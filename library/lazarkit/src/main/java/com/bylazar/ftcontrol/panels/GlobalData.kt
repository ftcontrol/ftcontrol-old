package com.bylazar.ftcontrol.panels

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.bylazar.ftcontrol.panels.integration.OpModeData.OpModeStatus
import com.bylazar.ftcontrol.panels.json.OpModeInfo
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta

object GlobalData {
    var opModeList: MutableList<OpModeInfo> = mutableListOf()

    var status = OpModeStatus.STOPPED

    var activeOpMode: OpMode? = null
    val activeOpModeInfo: OpModeInfo
        get() {
            for (opMode in opModeList) {
                if (opMode.name == activeOpModeName) {
                    return opMode
                }
            }
            return OpModeInfo(
                name = "\$Stop\$Robot\$",
                group = "",
                flavour = OpModeMeta.Flavor.AUTONOMOUS
            )
        }
    var activeOpModeName = ""

    var batteryVoltage = -1.0
}
