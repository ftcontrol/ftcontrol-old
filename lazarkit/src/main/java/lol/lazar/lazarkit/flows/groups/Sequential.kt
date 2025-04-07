package lol.lazar.lazarkit.flows.groups

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowBuilder
import lol.lazar.lazarkit.panels.json.JsonFlow
import lol.lazar.lazarkit.panels.json.SequentialJson
import java.util.UUID

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

    override val dependencyFlows: List<UUID>
        get(){
            val list = listOf<UUID>()
            flows.forEach {
                list + it.id
                list + it.dependencyFlows
            }
            return list
        }

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