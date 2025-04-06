package lol.lazar.lazarkit.flows

import lol.lazar.lazarkit.panels.json.InstantJson
import lol.lazar.lazarkit.panels.json.JsonFlow

fun instant(action: () -> Unit) = Instant(action)

open class Instant(var action: () -> Unit) : Flow() {
    override val toJson: JsonFlow
        get() = InstantJson()

    override fun innerAction() {
        println("   Running instant")
        action()
        finishedTime = System.currentTimeMillis()
    }
}