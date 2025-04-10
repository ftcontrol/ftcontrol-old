package com.bylazar.ftcontrol.flows

import com.bylazar.ftcontrol.panels.json.InstantJson
import com.bylazar.ftcontrol.panels.json.JsonFlow
import java.util.UUID

fun instant(action: () -> Unit) = Instant(action)

open class Instant(var action: () -> Unit) : Flow() {
    override val toJson: JsonFlow
        get() = InstantJson()

    override val dependencyFlows: List<UUID>
        get() = listOf()

    override fun innerAction() {
        println("   Running instant")
        action()
        finishedTime = System.currentTimeMillis()
    }
}