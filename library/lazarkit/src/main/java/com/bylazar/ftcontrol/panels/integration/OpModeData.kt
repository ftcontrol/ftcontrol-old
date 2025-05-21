package com.bylazar.ftcontrol.panels.integration

import com.qualcomm.ftccommon.FtcEventLoop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl
import com.bylazar.ftcontrol.panels.GlobalData
import com.bylazar.ftcontrol.panels.json.OpModeInfo
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta
import org.firstinspires.ftc.robotcore.internal.opmode.RegisteredOpModes

class OpModeData(
    private val onListChanged: (data: List<OpModeInfo>) -> Unit
) {
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

            val opModeList = ArrayList<OpModeInfo>()
            for (opModeMeta in RegisteredOpModes.getInstance().opModes) {
                if (opModeMeta.flavor != OpModeMeta.Flavor.SYSTEM) {
                    opModeList.add(
                        OpModeInfo(
                            name = opModeMeta.name,
                            group = opModeMeta.group,
                            flavour = opModeMeta.flavor,
                        )
                    )
                }
            }

            GlobalData.opModeList = opModeList

            onListChanged(opModeList)

            println("PANELS OPMODES: ${GlobalData.opModeList.joinToString(", ")}")
        }
    }
}