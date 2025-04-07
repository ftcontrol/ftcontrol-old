package lol.lazar.lazarkit.flows.groups

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowBuilder
import lol.lazar.lazarkit.panels.json.JsonFlow
import lol.lazar.lazarkit.panels.json.RaceJson
import java.util.UUID

fun race(block: FlowBuilder.() -> Unit) = Race(
    *FlowBuilder().apply(block).flows.toTypedArray()
)

class Race(
    vararg val flows: Flow
) : Flow() {
    override val toJson: JsonFlow
        get() = RaceJson(flows.map { it.id.toString() })

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
        if (flows.any { it.isFinished }) finishedTime = System.currentTimeMillis()

        flows.forEach {
            it.execute()
        }
    }
}