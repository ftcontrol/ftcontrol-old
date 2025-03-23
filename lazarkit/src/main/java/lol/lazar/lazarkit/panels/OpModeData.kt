package org.lazarkit.panels

import com.qualcomm.ftccommon.FtcEventLoop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta
import org.firstinspires.ftc.robotcore.internal.opmode.RegisteredOpModes
import org.lazarkit.panels.data.OpModeInfo

class OpModeData {
    enum class OpModeStatus {
        INIT,
        RUNNING,
        STOPPED
    }

    lateinit var opModeRegistry: OpModeManagerImpl

    fun setInit(opMode: OpMode) {
        GlobalData.activeOpMode = opMode
        GlobalData.activeOpModeName = opModeRegistry.activeOpModeName
        GlobalData.status = OpModeStatus.INIT
    }

    fun setRunning(opMode: OpMode) {
        GlobalData.activeOpMode = opMode
        GlobalData.activeOpModeName = opModeRegistry.activeOpModeName
        GlobalData.status = OpModeStatus.RUNNING
    }

    fun setStopped(opMode: OpMode) {
        GlobalData.activeOpMode = opMode
        GlobalData.activeOpModeName = opModeRegistry.activeOpModeName
        GlobalData.status = OpModeStatus.STOPPED

    }

    fun init(eventLoop: FtcEventLoop) {
        opModeRegistry = eventLoop.opModeManager
        GlobalData.activeOpMode = null
        GlobalData.activeOpModeName = ""
        GlobalData.opModeList.clear()
        val t = Thread(Routine())
        t.start()
    }

    inner class Routine : Runnable {
        override fun run() {
            RegisteredOpModes.getInstance().waitOpModesRegistered()

            GlobalData.opModeList.clear()
            for (opModeMeta in RegisteredOpModes.getInstance().opModes) {
                if (opModeMeta.flavor != OpModeMeta.Flavor.SYSTEM) {
                    GlobalData.opModeList.add(
                        OpModeInfo(
                            opModeMeta.name,
                            opModeMeta.group,
                            opModeMeta.flavor
                        )
                    )
                }
            }

            println("DASH OPMODES: ${GlobalData.opModeList.joinToString(", ")}")
        }
    }
}