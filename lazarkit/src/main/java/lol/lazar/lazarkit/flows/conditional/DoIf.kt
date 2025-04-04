package lol.lazar.lazarkit.flows.conditional

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowScope

fun doIf(condition: () -> Boolean, block: FlowScope.() -> Unit) = DoIf(
    condition = condition,
    flow = FlowScope().apply(block)
)

class DoIf(
    condition: () -> Boolean,
    flow: Flow
) : Flow(
    {
        if (condition()) {
            flow.execute()
        }
    }
)