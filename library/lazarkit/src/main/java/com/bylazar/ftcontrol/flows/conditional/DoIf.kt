package com.bylazar.ftcontrol.flows.conditional

import com.bylazar.ftcontrol.flows.EmptyFlow
import com.bylazar.ftcontrol.flows.Flow
import com.bylazar.ftcontrol.flows.FlowBuilder
import com.bylazar.ftcontrol.flows.groups.Sequential
import com.bylazar.ftcontrol.panels.json.DoIfJson
import com.bylazar.ftcontrol.panels.json.JsonFlow
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
        get() {
            val json = DoIfJson(passedCheck, flow.id.toString())
            json.id = id.toString()
            return json
        }

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
