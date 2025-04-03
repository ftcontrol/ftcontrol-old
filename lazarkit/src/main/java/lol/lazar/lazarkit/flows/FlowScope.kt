package lol.lazar.lazarkit.flows

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FlowScope(
    val flows: MutableList<Flow> = mutableListOf()
) : Flow({
    coroutineScope {
        flows.forEach { it.execute() }
    }
}, "FlowScope") {

    val flowDescription = StringBuilder()


    fun add(flow: Flow) {
        flows.add(flow)
    }

    fun seq(block: FlowScope.() -> Unit): Flow {
        flowDescription.append("seq { ")
        val flow = FlowScope().apply(block).also { add(it) }
        flowDescription.append("} ")
        return flow
    }

    fun para(block: FlowScope.() -> Unit): Flow {
        flowDescription.append("para { ")
        val flow = FlowScope().apply(block).also { add(it) }
        flowDescription.append("} ")
        return flow
    }

    fun race(block: FlowScope.() -> Unit): Flow {
        flowDescription.append("race { ")
        val flow = FlowScope().apply(block).also { add(it) }
        flowDescription.append("} ")
        return flow
    }

    fun instant(action: () -> Unit): Flow {
        flowDescription.append("instant { ... } ")
        return Flow({ action() }, "instant { ... }").also { add(it) }
    }

    fun waitFlow(durationMillis: Long): Flow {
        flowDescription.append("waitFlow($durationMillis) ")
        return Flow({ delay(durationMillis) }, "waitFlow($durationMillis)").also { add(it) }
    }

    fun ifFlow(condition: () -> Boolean, block: FlowScope.() -> Unit): Flow {
        flowDescription.append("ifFlow { ... } ")
        return Flow({
            if (condition()) {
                FlowScope().apply(block).execute()
            }
        }, "ifFlow { ... }").also { add(it) }
    }

    fun ifFlowSuspend(condition: suspend () -> Boolean, block: FlowScope.() -> Unit): Flow {
        flowDescription.append("ifFlowSuspend { ... } ")
        return Flow({
            if (condition()) {
                FlowScope().apply(block).execute()
            }
        }, "ifFlowSuspend { ... }").also { add(it) }
    }

    override suspend fun execute() {
        flows.forEach { it.execute() }
    }

    override fun describe(indent: Int): String {
        val indentString = "  ".repeat(indent)
        return buildString {
            appendLine("$indentString${flowDescription}")
            if (flows.isNotEmpty()) {
                appendLine("$indentString{")
                flows.forEach { flow ->
                    if (flow is FlowScope) {
                        append(flow.describe(indent + 1))
                    } else {
                        appendLine("$indentString  ${flow.description}")
                    }
                }
                appendLine("$indentString}")
            }
        }.trimEnd()
    }

    fun addDescription(description: String) {
        flowDescription.append("$description ")
    }
}

fun seq(block: FlowScope.() -> Unit): Flow = Flow({
    FlowScope().apply(block).execute()
}, "seq { ${FlowScope().apply(block).describe()} }")

fun para(block: FlowScope.() -> Unit): Flow = Flow({
    coroutineScope {
        FlowScope().apply(block).flows.forEach { launch { it.execute() } }
    }
}, "para { ${FlowScope().apply(block).describe()} }")

fun race(block: FlowScope.() -> Unit): Flow = Flow({
    coroutineScope {
        val jobs = FlowScope().apply(block).flows.map { async { it.execute() } }
        jobs.first().await()
        jobs.forEach { it.cancel() }
    }
}, "race { ${FlowScope().apply(block).describe()} }")

fun instant(action: () -> Unit): Flow = Flow({
    action()
}, "instant { ... }")

fun waitFlow(durationMillis: Long): Flow = Flow({
    delay(durationMillis)
}, "waitFlow($durationMillis)")

fun ifFlow(condition: () -> Boolean, block: FlowScope.() -> Unit): Flow = Flow({
    if (condition()) {
        FlowScope().apply(block).execute()
    }
}, "ifFlow { ${FlowScope().apply(block).describe()} }")

fun ifFlowSuspend(condition: suspend () -> Boolean, block: FlowScope.() -> Unit): Flow = Flow({
    if (condition()) {
        FlowScope().apply(block).execute()
    }
}, "ifFlowSuspend { ${FlowScope().apply(block).describe()} }")