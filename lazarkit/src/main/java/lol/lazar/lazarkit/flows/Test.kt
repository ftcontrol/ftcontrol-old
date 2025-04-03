package lol.lazar.lazarkit.flows

import kotlinx.coroutines.delay

class Test {
    var batteryLevel = 50

    val autonomous = seq {
        instant { println("DASH: FLOWS: Start moving") }
            .also { addDescription("Start moving") }

        waitFlow(1000)
            .also { addDescription("Wait for 1 second") }

        ifFlow({ batteryLevel > 20 }) {
            instant { println("DASH: FLOWS: Battery is good, proceeding") }
                .also { addDescription("Check battery level and proceed if good") }
        }.also { addDescription("Conditional block: Battery check") }

        ifFlowSuspend({ checkSensor() }) {
            instant { println("DASH: FLOWS: Sensor detected obstacle!") }
                .also { addDescription("Handle sensor detection") }
        }.also { addDescription("Conditional block: Sensor check") }

        instant { println("DASH: FLOWS: Done") }
            .also { addDescription("End of flow") }
    }

    private suspend fun checkSensor(): Boolean {
        delay(500)
        return true
    }

    fun printAutonomousFlowDescription() {
        println("DASH: FLOWS: Autonomous flow description:")
        println(autonomous.describe())
    }
}