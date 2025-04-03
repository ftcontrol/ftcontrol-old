package lol.lazar.lazarkit.flows.groups

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowScope

fun race(block: FlowScope.() -> Unit) = Race(
    *FlowScope().apply(block).flows.toTypedArray()
)

class Race(
    vararg flows: Flow
) : Flow(
    {
        coroutineScope {
            val jobs = flows.map { async { it.execute() } }
            jobs.first().await()
            jobs.forEach { it.cancel() }
        }
    }
)