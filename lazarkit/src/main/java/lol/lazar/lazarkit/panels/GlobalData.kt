package lol.lazar.lazarkit.panels

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import lol.lazar.lazarkit.panels.OpModeData.OpModeStatus
import lol.lazar.lazarkit.panels.configurables.ClassFinder
import lol.lazar.lazarkit.panels.configurables.GenericType
import lol.lazar.lazarkit.panels.configurables.VariablesManager
import lol.lazar.lazarkit.panels.data.OpModeInfo
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

    var configurableClasses: List<ClassFinder.ClassEntry> = listOf()
    var customTypeClasses: List<ClassFinder.ClassEntry> = listOf()

    var jvmFields = listOf<GenericType>()
}
