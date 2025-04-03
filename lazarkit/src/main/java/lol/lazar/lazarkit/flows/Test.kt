package lol.lazar.lazarkit.flows

import kotlinx.coroutines.delay

class Test {
    var batteryLevel = 50

    val autonomous = seq {
        instant { println("Start moving") }
        waitFlow(1000)

        ifFlow({ batteryLevel > 20 }) {
            instant { println("Battery is good, proceeding") }
        }

        ifFlowSuspend({ checkSensor() }) {
            instant { println("Sensor detected obstacle!") }
        }

        instant { println("Done") }
    }


    suspend fun checkSensor(): Boolean {
        delay(500)
        return true
    }
}