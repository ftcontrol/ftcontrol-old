package com.bylazar.ftcontrol.flows.groups

import com.bylazar.ftcontrol.flows.Flow
import com.bylazar.ftcontrol.flows.FlowBuilder
import com.bylazar.ftcontrol.panels.json.JsonFlow
import com.bylazar.ftcontrol.panels.json.SequentialJson
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
        get() {
            val json = SequentialJson(index, flows.map { it.id.toString() })
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