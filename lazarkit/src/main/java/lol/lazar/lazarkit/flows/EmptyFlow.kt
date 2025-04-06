package lol.lazar.lazarkit.flows

import lol.lazar.lazarkit.panels.json.EmptyJson
import lol.lazar.lazarkit.panels.json.JsonFlow

class EmptyFlow : Flow() {
    init {
        finishedTime = System.currentTimeMillis()
    }

    override fun innerAction() {}

    override val toJson: JsonFlow
        get() = EmptyJson()
}