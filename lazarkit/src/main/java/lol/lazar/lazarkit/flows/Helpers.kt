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