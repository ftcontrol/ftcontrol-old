package lol.lazar.lazarkit.flows

import kotlinx.coroutines.delay

class Test {
    var batteryLevel = 50

    val autonomous = seq {
        instant { println("DASH: FLOWS: Start moving") }

        waitFlow(1000)

        ifFlow({ batteryLevel > 20 }) {
            instant { println("DASH: FLOWS: Battery is good, proceeding") }
        }

        ifFlowSuspend({ checkSensor() }) {
            instant { println("DASH: FLOWS: Sensor detected obstacle!") }
        }

        instant { println("DASH: FLOWS: Done") }
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