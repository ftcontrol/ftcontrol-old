package lol.lazar.lazarkit.flows.conditional

import lol.lazar.lazarkit.flows.EmptyFlow
import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowBuilder
import lol.lazar.lazarkit.flows.groups.Sequential
import lol.lazar.lazarkit.panels.json.DoIfJson
import lol.lazar.lazarkit.panels.json.JsonFlow
import java.util.UUID

fun doIf(condition: () -> Boolean, block: FlowBuilder.() -> Unit): DoIf {
    val flows = FlowBuilder().apply(block).flows
    val flow = when (flows.size) {
        0 -> EmptyFlow()
        1 -> flows[0]
        else -> Sequential(*flows.toTypedArray())
    }
    return DoIf(condition, flow)
}

class DoIf(
    private val condition: () -> Boolean,
    private val flow: Flow
) : Flow() {

    private var passedCheck = false

    override val toJson: JsonFlow
        get() = DoIfJson(passedCheck, flow.id.toString())

    override val dependencyFlows: List<UUID>
        get() = listOf<UUID>() + flow.id + flow.dependencyFlows

    override fun innerAction() {
        if (!passedCheck) {
            passedCheck = condition()
            if (!passedCheck) {
                finishedTime = System.currentTimeMillis()
                return
            }
        }

        flow.execute()
        if (flow.isFinished) {
            finishedTime = System.currentTimeMillis()
        }
    }
}
