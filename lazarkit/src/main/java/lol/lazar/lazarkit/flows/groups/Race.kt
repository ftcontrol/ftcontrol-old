package lol.lazar.lazarkit.flows.groups

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowScope

fun race(block: FlowScope.() -> Unit) = Race(
    *FlowScope().apply(block).flows.toTypedArray()
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