package lol.lazar.lazarkit.flows.groups

import kotlinx.coroutines.coroutineScope
import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowScope

fun sequential(block: FlowScope.() -> Unit) = Sequential(
    *FlowScope().apply(block).flows.toTypedArray()
)

class Sequential(
    vararg flows: Flow
) : Flow(
    {
        coroutineScope {
            flows.forEach { it.execute() }
        }
    }
)