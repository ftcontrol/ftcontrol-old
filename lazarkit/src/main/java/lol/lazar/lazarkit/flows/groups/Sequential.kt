package lol.lazar.lazarkit.flows.groups

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowBuilder

fun sequential(block: FlowBuilder.() -> Unit) = Sequential(
    *FlowBuilder().apply(block).flows.toTypedArray()
)

class Sequential(
    vararg val flows: Flow
) : Flow({}) {
    var index = 0
        private set

    override fun innerAction() {
        if (index < flows.size) {
            val flow = flows[index]

            if (!flow.isFinished) {
                flow.execute()
            } else {
                index++
            }
        } else {
            finishedTime = System.currentTimeMillis()
        }
    }
}