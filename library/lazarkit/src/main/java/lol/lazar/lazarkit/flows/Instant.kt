package lol.lazar.lazarkit.flows

import lol.lazar.lazarkit.panels.json.InstantJson
import lol.lazar.lazarkit.panels.json.JsonFlow
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