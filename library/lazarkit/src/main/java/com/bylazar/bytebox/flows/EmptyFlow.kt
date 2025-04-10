package com.bylazar.bytebox.flows

import com.bylazar.bytebox.panels.json.EmptyJson
import com.bylazar.bytebox.panels.json.JsonFlow
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