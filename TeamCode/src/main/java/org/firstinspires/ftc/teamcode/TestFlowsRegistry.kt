package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlinx.coroutines.runBlocking
import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowRegistry
import lol.lazar.lazarkit.panels.Panels

@TeleOp(name = "TestFlowsRegistry", group = "Dashboard")
class TestFlowsRegistry : OpMode() {

    val panelsTelemetry = Panels.getTelemetry()

    override fun init() {
        runBlocking {
            FlowRegistry.init()
            FlowRegistry.register(Flow {
                panelsTelemetry.debug("Flow", "Executing...")
                panelsTelemetry.update(telemetry)
            })
        }
    }

    override fun loop() {
        runBlocking {
            FlowRegistry.loop()
        }
    }
}
