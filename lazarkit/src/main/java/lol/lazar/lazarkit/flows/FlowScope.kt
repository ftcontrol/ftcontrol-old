package lol.lazar.lazarkit.flows

import kotlinx.coroutines.coroutineScope
import lol.lazar.lazarkit.flows.ifFlow as ifFlowHelper
import lol.lazar.lazarkit.flows.ifFlowSuspend as ifFlowSuspendHelper
import lol.lazar.lazarkit.flows.instant as instantHelper
import lol.lazar.lazarkit.flows.para as paraHelper
import lol.lazar.lazarkit.flows.race as raceHelper
import lol.lazar.lazarkit.flows.seq as seqHelper
import lol.lazar.lazarkit.flows.waitFlow as waitFlowHelper

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

    fun seq(block: FlowScope.() -> Unit): Flow = seqHelper(block).also { add(it) }

    fun para(block: FlowScope.() -> Unit): Flow = paraHelper(block).also { add(it) }

    fun race(block: FlowScope.() -> Unit): Flow = raceHelper(block).also { add(it) }

    fun instant(action: () -> Unit): Flow = instantHelper(action).also { add(it) }

    fun waitFlow(durationMillis: Long): Flow = waitFlowHelper(durationMillis).also { add(it) }

    fun ifFlow(condition: () -> Boolean, block: FlowScope.() -> Unit): Flow =
        ifFlowHelper(condition, block).also { add(it) }

    fun ifFlowSuspend(condition: suspend () -> Boolean, block: FlowScope.() -> Unit): Flow =
        ifFlowSuspendHelper(condition, block).also { add(it) }
}