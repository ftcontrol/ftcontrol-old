package lol.lazar.lazarkit.flows.conditional

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowScope

fun must(condition: () -> Boolean, block: FlowScope.() -> Unit) = Must(
    condition = condition,
    flow = FlowScope().apply(block)
)

class Must(
    condition: () -> Boolean,
    flow: Flow
) : Flow(
    {
        if (condition()) {
            flow.execute()
        }
    }
)