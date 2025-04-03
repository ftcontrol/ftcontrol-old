package lol.lazar.lazarkit.flows.groups

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowScope

fun parallel(block: FlowScope.() -> Unit) = Parallel(
    *FlowScope().apply(block).flows.toTypedArray()
)

class Parallel(
    vararg flows: Flow
) : Flow(
    {
        coroutineScope {
            flows.forEach { launch { it.execute() } }
        }
    }
)