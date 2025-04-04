package lol.lazar.lazarkit.flows.conditional

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowScope


fun doIfSuspended(condition: suspend () -> Boolean, block: FlowScope.() -> Unit) = DoIfSuspended(
    condition = condition,
    flow = FlowScope().apply(block)
)

class DoIfSuspended(
    condition: suspend () -> Boolean,
    flow: Flow
) : Flow(
    {
        if (condition()) {
            flow.execute()
        }
    }
)