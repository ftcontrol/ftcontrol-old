package lol.lazar.lazarkit.flows

import kotlinx.coroutines.coroutineScope
import lol.lazar.lazarkit.flows.conditional.must as mustHelper
import lol.lazar.lazarkit.flows.conditional.mustSuspended as mustSuspendedHelper
import lol.lazar.lazarkit.flows.groups.parallel as parallelHelper
import lol.lazar.lazarkit.flows.groups.race as raceHelper
import lol.lazar.lazarkit.flows.groups.sequential as sequentialHelper
import lol.lazar.lazarkit.flows.instant as instantHelper
import lol.lazar.lazarkit.flows.wait as waitHelper

class FlowScope(
    val flows: MutableList<Flow> = mutableListOf()
) : Flow({
    coroutineScope {
        flows.forEach { it.execute() }
    }
}) {

    fun add(flow: Flow) {
        flows.add(flow)
    }

    fun sequential(block: FlowScope.() -> Unit) = sequentialHelper(block).also { add(it) }

    fun parallel(block: FlowScope.() -> Unit) = parallelHelper(block).also { add(it) }

    fun race(block: FlowScope.() -> Unit) = raceHelper(block).also { add(it) }

    fun instant(action: () -> Unit) = instantHelper(action).also { add(it) }

    fun wait(durationMillis: Long) = waitHelper(durationMillis).also { add(it) }

    fun must(condition: () -> Boolean, block: FlowScope.() -> Unit) =
        mustHelper(condition, block).also { add(it) }

    fun mustSuspended(condition: suspend () -> Boolean, block: FlowScope.() -> Unit) =
        mustSuspendedHelper(condition, block).also { add(it) }

    override fun describe(indent: Int): String {
        var str = ""
        flows.forEach { flow ->
            if (flow is FlowScope) {
                str += "${"  ".repeat(indent)}${flow.describe(indent + 1)}\n"
            } else {
                str += "${"  ".repeat(indent)}${flow.describe(indent)}\n"
            }
        }
        return str
    }
}