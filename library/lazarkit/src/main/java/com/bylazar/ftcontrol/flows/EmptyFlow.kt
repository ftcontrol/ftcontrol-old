package com.bylazar.ftcontrol.flows

import com.bylazar.ftcontrol.panels.json.EmptyJson
import com.bylazar.ftcontrol.panels.json.JsonFlow
import java.util.UUID

class EmptyFlow : Flow() {
    init {
        finishedTime = System.currentTimeMillis()
    }

    override fun innerAction() {}

    override val toJson: JsonFlow
        get() = EmptyJson()

    override val dependencyFlows: List<UUID>
        get() = listOf()
}