package com.bylazar.bytebox.flows

import com.bylazar.bytebox.panels.json.InstantJson
import com.bylazar.bytebox.panels.json.JsonFlow
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