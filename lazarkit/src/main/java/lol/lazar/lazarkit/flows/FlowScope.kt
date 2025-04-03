package lol.lazar.lazarkit.flows

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun seq(block: FlowScope.() -> Unit): Flow = Flow {
    FlowScope().apply(block).execute()
}

fun para(block: FlowScope.() -> Unit): Flow = Flow {
    coroutineScope {
        FlowScope().apply(block).flows.forEach { launch { it.execute() } }
    }
}

fun race(block: FlowScope.() -> Unit): Flow = Flow {
    coroutineScope {
        val jobs = FlowScope().apply(block).flows.map { async { it.execute() } }
        jobs.first().await()
        jobs.forEach { it.cancel() }
    }
}

fun instant(action: () -> Unit): Flow = Flow {
    action()
}

fun waitFlow(durationMillis: Long): Flow = Flow {
    delay(durationMillis)
}

fun ifFlow(condition: () -> Boolean, block: FlowScope.() -> Unit): Flow = Flow {
    if (condition()) {
        FlowScope().apply(block).execute()
    }
}

fun ifFlowSuspend(condition: suspend () -> Boolean, block: FlowScope.() -> Unit): Flow = Flow {
    if (condition()) {
        FlowScope().apply(block).execute()
    }
}

class FlowScope {
    internal val flows = mutableListOf<Flow>()

    fun add(flow: Flow) {
        flows.add(flow)
    }

    fun seq(block: FlowScope.() -> Unit): Flow = seq(block).also { add(it) }
    fun para(block: FlowScope.() -> Unit): Flow = para(block).also { add(it) }
    fun race(block: FlowScope.() -> Unit): Flow = race(block).also { add(it) }
    fun instant(action: () -> Unit): Flow = instant(action).also { add(it) }
    fun waitFlow(durationMillis: Long): Flow = waitFlow(durationMillis).also { add(it) }

    fun ifFlow(condition: () -> Boolean, block: FlowScope.() -> Unit): Flow = ifFlow(condition, block).also { add(it) }
    fun ifFlowSuspend(condition: suspend () -> Boolean, block: FlowScope.() -> Unit): Flow = ifFlowSuspend(condition, block).also { add(it) }

    suspend fun execute() {
        flows.forEach { it.execute() }
    }
}