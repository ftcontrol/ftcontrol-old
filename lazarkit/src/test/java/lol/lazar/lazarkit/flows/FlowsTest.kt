package lol.lazar.lazarkit.flows

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import lol.lazar.lazarkit.core.Entity
import lol.lazar.lazarkit.flows.groups.parallel
import lol.lazar.lazarkit.flows.groups.race
import lol.lazar.lazarkit.flows.groups.sequential
import org.firstinspires.ftc.robotcore.internal.system.Assert.assertTrue
import org.junit.Test
import kotlin.system.measureTimeMillis

class FlowsTest {

    @Test
    fun `test sequential execution order`() = runBlocking {
        val executionOrder = mutableListOf<String>()

        val flow = sequential {
            instant { executionOrder.add("Step 1") }
            wait(500)
            instant { executionOrder.add("Step 2") }
            wait(500)
            instant { executionOrder.add("Step 3") }
        }

        flow.execute()

        println("Execution Order: $executionOrder")

        assertTrue(
            executionOrder == listOf("Step 1", "Step 2", "Step 3"),
            "Steps should execute in order"
        )
    }

    @Test
    fun `test parallel execution timing`() = runBlocking {
        val timeTaken = measureTimeMillis {
            parallel {
                wait(1000)
                wait(1000)
            }.execute()
        }

        println("Time taken for parallel execution: $timeTaken ms")

        assertTrue(timeTaken in 900..1100, "Parallel execution should finish in ~1000ms")
    }

    @Test
    fun `test entity-specific flow execution`() = runBlocking {
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

        flow.execute()

        println("Executed entity actions: $entityExecuted")

        assertTrue("Entity 1 Action" in entityExecuted, "Entity 1 should execute")
        assertTrue("Entity 2 Action" in entityExecuted, "Entity 2 should execute")
    }

    @Test
    fun `test conditional execution with doIf`() = runBlocking {
        var batteryLevel = 25
        var message = ""

        val flow = sequential {
            doIf({ batteryLevel > 20 }) {
                instant { message = "Battery good" }
            }
        }

        flow.execute()

        println("Message: $message")

        assertTrue(message == "Battery good", "Condition should be met and executed")
    }

    @Test
    fun `test suspended condition with doIfSuspended`() = runBlocking {
        var message = ""

        val flow = sequential {
            doIfSuspended({ checkSensor() }) {
                instant { message = "Obstacle detected" }
            }
        }

        flow.execute()

        println("Message: $message")

        assertTrue(message == "Obstacle detected", "Suspended condition should execute correctly")
    }

    private suspend fun checkSensor(): Boolean {
        delay(500)
        return true
    }

    @Test
    fun `test nested sequential and parallel execution`() = runBlocking {
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

        flow.execute()

        println("Execution Order: $executionOrder")

        assertTrue(executionOrder.first() == "Start", "First step should be 'Start'")
        assertTrue(executionOrder.last() == "End", "Last step should be 'End'")
    }


    @Test
    fun `parallel execution with same entity should behave sequentially`() = runBlocking {
        val entity = Entity()
        val executionOrder = mutableListOf<String>()

        val timeTaken = measureTimeMillis {
            parallel {
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
            }.execute()
        }

        println("Execution Order: $executionOrder")
        println("Time Taken: $timeTaken ms")

        assertTrue(
            executionOrder == listOf("Task 1 Start", "Task 1 End", "Task 2 Start", "Task 2 End"),
            "Tasks should execute sequentially since they share the same entity"
        )
        assertTrue(
            timeTaken >= 2000,
            "Total execution should take around 2000ms since they are forced sequentially"
        )
    }

    @Test
    fun `parallel execution with different entities should run concurrently`() = runBlocking {
        val entity1 = Entity()
        val entity2 = Entity()
        val executionOrder = mutableListOf<String>()

        val timeTaken = measureTimeMillis {
            parallel {
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
            }.execute()
        }

        println("Execution Order: $executionOrder")
        println("Time Taken: $timeTaken ms")

        assertTrue(
            timeTaken in 900..1100,
            "Total execution should take ~1000ms since they are truly parallel"
        )
    }

    @Test
    fun `sequential flow should reset entity busy state after execution`() = runBlocking {
        val entity = Entity()
        var wasBusyDuringFlow = false

        val flow = sequential {
            instant {
                wasBusyDuringFlow = entity.isBusy
            }.apply { entityId = entity.id }
            wait(500)
        }

        flow.execute()

        println("Entity busy state after execution: ${entity.isBusy}")

        assertTrue(wasBusyDuringFlow, "Entity should be marked busy during execution")
        assertTrue(!entity.isBusy, "Entity should be free after execution")
    }

    @Test
    fun `parallel flow should not start if entity is already busy`() = runBlocking {
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
                    if (!entity.isBusy) {
                        executedTask = "Second Task"
                    }
                }
            }.apply { entityId = entity.id }
        }

        flow.execute()

        println("Executed Task: $executedTask")

        assertTrue(
            executedTask == "First Task",
            "Second task should not execute since entity is busy"
        )
    }
}
