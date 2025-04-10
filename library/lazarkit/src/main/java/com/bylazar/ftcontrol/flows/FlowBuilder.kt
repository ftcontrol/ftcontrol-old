package com.bylazar.ftcontrol.flows

import com.bylazar.ftcontrol.flows.conditional.doIf as doIfHelper
import com.bylazar.ftcontrol.flows.groups.parallel as parallelHelper
import com.bylazar.ftcontrol.flows.groups.race as raceHelper
import com.bylazar.ftcontrol.flows.groups.sequential as sequentialHelper
import com.bylazar.ftcontrol.flows.instant as instantHelper
import com.bylazar.ftcontrol.flows.wait as waitHelper

class FlowBuilder(
    val flows: MutableList<Flow> = mutableListOf()
) {
    fun add(flow: Flow) {
        flows.add(flow)
    }

    fun sequential(block: FlowBuilder.() -> Unit) = sequentialHelper(block).also { add(it) }

    fun parallel(block: FlowBuilder.() -> Unit) = parallelHelper(block).also { add(it) }

    fun race(block: FlowBuilder.() -> Unit) = raceHelper(block).also { add(it) }

    fun instant(action: () -> Unit) = instantHelper(action).also { add(it) }

    fun wait(durationMillis: Long) = waitHelper(durationMillis).also { add(it) }

    fun doIf(condition: () -> Boolean, block: FlowBuilder.() -> Unit) =
        doIfHelper(condition, block).also { add(it) }
}