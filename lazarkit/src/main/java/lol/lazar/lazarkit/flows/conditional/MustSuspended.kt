package lol.lazar.lazarkit.flows.conditional

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowScope


fun mustSuspended(condition: suspend () -> Boolean, block: FlowScope.() -> Unit) = MustSuspended(
    condition = condition,
    flow = FlowScope().apply(block)
)

class MustSuspended(
    condition: suspend () -> Boolean,
    flow: Flow
) : Flow(
    {
        if (condition()) {
            flow.execute()
        }
    }
)