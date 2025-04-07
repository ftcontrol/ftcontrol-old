package lol.lazar.lazarkit.flows.groups

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowBuilder
import lol.lazar.lazarkit.panels.json.JsonFlow
import lol.lazar.lazarkit.panels.json.ParallelJson
import java.util.UUID

fun parallel(block: FlowBuilder.() -> Unit) = Parallel(
    *FlowBuilder().apply(block).flows.toTypedArray()
)

class Parallel(
    vararg val flows: Flow
) : Flow() {
    override val toJson: JsonFlow
        get() {
            val json = ParallelJson(flows.map { it.id.toString() })
            json.id = id.toString()
            return json
        }

    override val dependencyFlows: List<UUID>
        get() {
            val list = listOf<UUID>()
            flows.forEach {
                list + it.id
                list + it.dependencyFlows
            }
            return list
        }

    override fun innerAction() {
        if (!flows.any { !it.isFinished }) finishedTime = System.currentTimeMillis()

        flows.forEach {
            it.execute()
        }
    }
}