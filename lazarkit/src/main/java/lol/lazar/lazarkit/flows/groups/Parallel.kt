package lol.lazar.lazarkit.flows.groups

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowScope

fun parallel(block: FlowScope.() -> Unit) = Parallel(
    *FlowScope().apply(block).flows.toTypedArray()
)

class Parallel(
    vararg val flows: Flow
) : Flow({}) {
    override fun innerAction() {
        if (!flows.any { !it.isFinished }) finishedTime = System.currentTimeMillis()

        flows.forEach {
            it.execute()
        }
    }
}