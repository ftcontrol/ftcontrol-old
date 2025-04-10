package com.bylazar.bytebox.flows.groups

import com.bylazar.bytebox.flows.Flow
import com.bylazar.bytebox.flows.FlowBuilder
import com.bylazar.bytebox.panels.json.JsonFlow
import com.bylazar.bytebox.panels.json.RaceJson
import java.util.UUID

fun race(block: FlowBuilder.() -> Unit) = Race(
    *FlowBuilder().apply(block).flows.toTypedArray()
)

class Race(
    vararg val flows: Flow
) : Flow() {
    override val toJson: JsonFlow
        get() {
            val json = RaceJson(flows.map { it.id.toString() })
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
        if (flows.any { it.isFinished }) finishedTime = System.currentTimeMillis()

        flows.forEach {
            it.execute()
        }
    }
}