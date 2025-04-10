package com.bylazar.bytebox.flows

import com.bylazar.bytebox.core.Entity
import com.bylazar.bytebox.flows.conditional.doIf
import com.bylazar.bytebox.flows.groups.parallel
import com.bylazar.bytebox.flows.groups.race
import com.bylazar.bytebox.flows.groups.sequential
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.system.measureTimeMillis

class FlowsTest {

    private fun runFlow(flow: Flow) {
        while (!flow.isFinished) {
            flow.execute()
        }
    }

    @Test
    fun `test sequential execution order`() {
        val executionOrder = mutableListOf<String>()

        val flow = sequential {
            instant { executionOrder.add("Step 1") }
            wait(500)
            instant { executionOrder.add("Step 2") }
            wait(500)
            instant { executionOrder.add("Step 3") }
        }

        runFlow(flow)

        println("Execution Order: $executionOrder")
        assertTrue(
            executionOrder == listOf("Step 1", "Step 2", "Step 3")
        )
    }

    @Test
    fun `test parallel execution timing`() {
        val flow = parallel {
            wait(1000)
            wait(1000)
        }

        val timeTaken = measureTimeMillis {
            runFlow(flow)
        }

        println("Time taken for parallel execution: $timeTaken ms")
        assertTrue(timeTaken in 900..1100)
    }

    @Test
    fun `test entity-specific flow execution`() {
        val entity1 = Entity()
        val entity2 = Entity()
        val entityExecuted = mutableSetOf<String>()

        val flow = parallel {
            sequential {
                instant { entityExecuted.add("Entity 1 Action") }
                wait(500)
            }.apply { entityId = entity1.id }

            sequential {
                instant { entityExecuted.add("Entity 2 Action") }
                wait(500)
            }.apply { entityId = entity2.id }
        }

        runFlow(flow)

        println("Executed entity actions: $entityExecuted")
        assertTrue("Entity 1 Action" in entityExecuted)
        assertTrue("Entity 2 Action" in entityExecuted)
    }

    @Test
    fun `test conditional execution with doIf`() {
        val batteryLevel = 25
        var message = false

        val flow = doIf({ batteryLevel > 20 }) {
            instant {
                println("Running instant block")
                message = true
            }
        }

        println("Message: $message")

        runFlow(flow)
        assertTrue(message)
    }

    @Test
    fun `test nested sequential and parallel execution`() {
        val executionOrder = mutableListOf<String>()

        val flow = sequential {
            instant { executionOrder.add("Start") }

            parallel {
                sequential {
                    instant { executionOrder.add("Seq1-Step1") }
                    wait(500)
                    instant { executionOrder.add("Seq1-Step2") }
                }

                sequential {
                    instant { executionOrder.add("Seq2-Step1") }
                    wait(1000)
                    instant { executionOrder.add("Seq2-Step2") }
                }
            }

            instant { executionOrder.add("End") }
        }

        runFlow(flow)

        println("Execution Order: $executionOrder")
        assertTrue(executionOrder.first() == "Start")
        assertTrue(executionOrder.last() == "End")
    }

    @Test
    fun `parallel execution with same entity should behave sequentially`() {
        val entity = Entity()
        val executionOrder = mutableListOf<String>()

        val flow = parallel {
            sequential {
                instant {
                    executionOrder.add("Task 1 Start")
                }
                wait(1000)
                instant {
                    executionOrder.add("Task 1 End")
                }
            }.apply { entityId = entity.id }

            sequential {
                instant {
                    executionOrder.add("Task 2 Start")
                }
                wait(1000)
                instant {
                    executionOrder.add("Task 2 End")
                }
            }.apply { entityId = entity.id }
        }

        val timeTaken = measureTimeMillis {
            runFlow(flow)
        }

        println("Execution Order: $executionOrder")
        println("Time Taken: $timeTaken ms")
        assertTrue(
            executionOrder == listOf("Task 1 Start", "Task 1 End", "Task 2 Start", "Task 2 End")
        )
        assertTrue(
            timeTaken >= 2000
        )
    }

    @Test
    fun `parallel execution with different entities should run concurrently`() {
        val entity1 = Entity()
        val entity2 = Entity()
        val executionOrder = mutableListOf<String>()

        val flow = parallel {
            sequential {
                instant {
                    executionOrder.add("Entity1 Task Start")
                }
                wait(1000)
                instant {
                    executionOrder.add("Entity1 Task End")
                }
            }.apply { entityId = entity1.id }

            sequential {
                instant {
                    executionOrder.add("Entity2 Task Start")
                }
                wait(1000)
                instant {
                    executionOrder.add("Entity2 Task End")
                }
            }.apply { entityId = entity2.id }
        }

        val timeTaken = measureTimeMillis {
            runFlow(flow)
        }

        println("Execution Order: $executionOrder")
        println("Time Taken: $timeTaken ms")
        assertTrue(
            timeTaken in 900..1100
        )
    }

    @Test
    fun `sequential flow should reset entity busy state after execution`() {
        val entity = Entity()
        var wasBusyDuringFlow = false

        val flow = sequential {
            instant {
                wasBusyDuringFlow = entity.isBusy
            }.apply { entityId = entity.id }
            wait(500)
        }

        runFlow(flow)

        println("Entity busy state after execution: ${entity.isBusy}")
        assertTrue(wasBusyDuringFlow)
        assertTrue(!entity.isBusy)
    }

    @Test
    fun `parallel flow should not start if entity is already busy`() {
        val entity = Entity()
        var executedTask = ""

        val flow = race {
            sequential {
                instant {
                    executedTask = "First Task"
                }
                wait(1000)
            }.apply { entityId = entity.id }

            sequential {
                instant {
                    // This task should not execute because the entity is busy
                    if (!entity.isBusy) {
                        executedTask = "Second Task"
                    }
                }
            }.apply { entityId = entity.id }
        }

        runFlow(flow)

        println("Executed Task: $executedTask")
        assertTrue(
            executedTask == "First Task"
        )
    }
}
