package lol.lazar.lazarkit.flows

import kotlinx.coroutines.delay
import lol.lazar.lazarkit.flows.conditional.mustSuspended
import lol.lazar.lazarkit.flows.groups.sequential

class Test {
    var batteryLevel = 50

    val sequential = sequential { }
    val stuff = sequential

    val autonomous = sequential {
        instant { println("DASH: FLOWS: Start moving") }

        wait(1000)

        must({ batteryLevel > 20 }) {
            instant { println("DASH: FLOWS: Battery is good, proceeding") }
        }

        mustSuspended({ checkSensor() }) {
            instant { println("DASH: FLOWS: Sensor detected obstacle!") }
        }

        parallel {
            instant { println("DASH: FLOWS: Parallel flow start") }
            instant { println("DASH: FLOWS: Parallel flow start") }
        }

        instant { println("DASH: FLOWS: Done") }
    }

    private suspend fun checkSensor(): Boolean {
        delay(500)
        return true
    }

    fun printAutonomousFlowDescription() {
        println("DASH: FLOWS: Autonomous flow description:")
        println("DASH: FLOWS: ${autonomous.describe()}")
    }
}