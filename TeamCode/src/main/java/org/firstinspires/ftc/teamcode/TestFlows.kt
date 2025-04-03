package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import lol.lazar.lazarkit.LoopTimer
import lol.lazar.lazarkit.flows.Test
import lol.lazar.lazarkit.panels.Panels
import lol.lazar.lazarkit.panels.configurables.annotations.Configurable

@Configurable
@TeleOp(name = "Testing Flows OpMode", group = "Dashboard")
class TestFlows : OpMode() {
    var timer = LoopTimer()

    var panelsTelemetry = Panels.getTelemetry()

    val test = Test()

    private var job: Job? = null

    override fun init() {
        panelsTelemetry.debug("Init was ran!")
        test.printAutonomousFlowDescription()
        panelsTelemetry.debug(test.autonomous.describe())
        panelsTelemetry.update(telemetry)
    }

    override fun start() {
        job = CoroutineScope(Dispatchers.Default).launch {
            test.autonomous.execute()
        }
    }

    override fun loop() {
        timer.start()
        panelsTelemetry.debug("LoopTime: ${timer.hz}")

        panelsTelemetry.update(telemetry)
        timer.end()
    }

    override fun stop() {
        job?.cancel()
    }
}