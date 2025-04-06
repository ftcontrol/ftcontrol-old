package lol.lazar.lazarkit.flows.groups

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowBuilder
import lol.lazar.lazarkit.panels.json.JsonFlow
import lol.lazar.lazarkit.panels.json.SequentialJson

fun sequential(block: FlowBuilder.() -> Unit) = Sequential(
    *FlowBuilder().apply(block).flows.toTypedArray()
)

class Sequential(
    vararg val flows: Flow
) : Flow() {
    var index = 0
        private set

    override val toJson: JsonFlow
        get() = SequentialJson(index, flows.map { it.id.toString() })

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