package org.lazarkit.panels

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.lazarkit.panels.OpModeData.OpModeStatus
import org.lazarkit.panels.data.OpModeInfo

object GlobalData {
    val opModeList: MutableList<OpModeInfo> = mutableListOf()
    val opModeListString: String
        get() = opModeList.joinToString(".") {
            "${it.name}.${it.group}.${it.flavour}"
        }

    var status = OpModeStatus.STOPPED

    var activeOpMode: OpMode? = null
    var activeOpModeName = ""
}