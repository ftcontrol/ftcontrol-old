package lol.lazar.lazarkit.flows

import lol.lazar.lazarkit.flows.conditional.doIf as doIfHelper
import lol.lazar.lazarkit.flows.groups.parallel as parallelHelper
import lol.lazar.lazarkit.flows.groups.race as raceHelper
import lol.lazar.lazarkit.flows.groups.sequential as sequentialHelper
import lol.lazar.lazarkit.flows.instant as instantHelper
import lol.lazar.lazarkit.flows.wait as waitHelper

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