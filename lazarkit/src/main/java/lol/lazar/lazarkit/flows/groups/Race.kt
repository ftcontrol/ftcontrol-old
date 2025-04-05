package lol.lazar.lazarkit.flows.groups

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowBuilder

fun race(block: FlowBuilder.() -> Unit) = Race(
    *FlowBuilder().apply(block).flows.toTypedArray()
)

class Race(
    vararg val flows: Flow
) : Flow({}) {
    override fun innerAction() {
        if (flows.any { it.isFinished }) finishedTime = System.currentTimeMillis()

        flows.forEach {
            it.execute()
        }
    }
}