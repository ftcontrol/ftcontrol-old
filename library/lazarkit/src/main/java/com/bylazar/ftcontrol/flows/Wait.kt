package com.bylazar.ftcontrol.flows

import com.bylazar.ftcontrol.panels.json.JsonFlow
import com.bylazar.ftcontrol.panels.json.WaitJson
import java.util.UUID


fun wait(durationMillis: Long) = Wait(durationMillis)

class Wait(val durationMillis: Long) : Flow() {
    var startedTimestamp = -1L
    override fun innerAction() {
        if (startedTimestamp == -1L) {
            startedTimestamp = System.currentTimeMillis()
        }

        if (System.currentTimeMillis() - startedTimestamp >= durationMillis) {
            finishedTime = System.currentTimeMillis()
        }
    }

    override val toJson: JsonFlow
        get() = WaitJson(startedTimestamp, durationMillis)

    override val dependencyFlows: List<UUID>
        get() = listOf()
}