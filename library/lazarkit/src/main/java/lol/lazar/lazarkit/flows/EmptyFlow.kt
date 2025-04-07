package lol.lazar.lazarkit.flows

import lol.lazar.lazarkit.panels.json.EmptyJson
import lol.lazar.lazarkit.panels.json.JsonFlow
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