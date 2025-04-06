package lol.lazar.lazarkit.flows.groups

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowBuilder
import lol.lazar.lazarkit.panels.json.JsonFlow
import lol.lazar.lazarkit.panels.json.ParallelJson

fun parallel(block: FlowBuilder.() -> Unit) = Parallel(
    *FlowBuilder().apply(block).flows.toTypedArray()
)

class Parallel(
    vararg val flows: Flow
) : Flow() {
    override val toJson: JsonFlow
        get() = ParallelJson(flows.map { it.id.toString() })
    override fun innerAction() {
        if (!flows.any { !it.isFinished }) finishedTime = System.currentTimeMillis()

        flows.forEach {
            it.execute()
        }
    }
}