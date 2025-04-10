package com.bylazar.ftcontrol.flows

import com.bylazar.ftcontrol.core.Entity
import com.bylazar.ftcontrol.flows.groups.sequential

class Test {
    var batteryLevel = 50

    val entity = Entity()

    val entity2 = Entity()

    val autonomous = sequential {
        val lazar = "test"

        instant { println("DASH: FLOWS: $lazar") }

        instant { println("DASH: FLOWS: Start moving") }

        instant { println("DASH: FLOWS: Start time: ${System.currentTimeMillis()}") }

        parallel {
            sequential {
                instant { println("DASH: FLOWS: LAZAR1") }
                wait(1000)
            }.apply { entityId = entity.id }
            sequential {
                instant { println("DASH: FLOWS: LAZAR2") }
                wait(2000)
            }.apply { entityId = entity2.id }
        }

        instant { println("DASH: FLOWS: End time: ${System.currentTimeMillis()}") }

        wait(1000)

        doIf({ batteryLevel > 20 }) {
            instant { println("DASH: FLOWS: Battery is good, proceeding") }
        }

        parallel {
            instant { println("DASH: FLOWS: Parallel flow start") }
            instant { println("DASH: FLOWS: Parallel flow start") }
        }

        instant { println("DASH: FLOWS: Done") }
    }

    fun printAutonomousFlowDescription() {
        println("DASH: FLOWS: Autonomous flow description:")
        println("DASH: FLOWS: ${autonomous.describe()}")
    }
}