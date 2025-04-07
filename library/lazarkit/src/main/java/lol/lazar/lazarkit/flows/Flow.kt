package lol.lazar.lazarkit.flows

import lol.lazar.lazarkit.core.GlobalEntities
import lol.lazar.lazarkit.panels.json.JsonFlow
import java.util.UUID

abstract class Flow {
    init {
        FlowRegistry.allFlows.add(this)
    }

    abstract fun innerAction()

    val id = UUID.randomUUID()
    var description: String = ""
    var entityId: String? = null

    var finishedTime: Long = -1L
    val isFinished: Boolean get() = finishedTime != -1L

    open fun execute() {
        if (isFinished) return
        val entity = GlobalEntities.entities[entityId]
        if (entity != null) {
            if (entity.isBusy && entity.runningFlowId != id) return
            entity.lock(id)

            try {
                innerAction()
            } catch (e: Throwable) {
                entity.unlock()
                throw e
            }
        } else {
            innerAction()
        }

        if (isFinished && entity != null) {
            entity.unlock()
        }
    }

    open fun describe(indent: Int): String =
        "${"  ".repeat(indent)}$description"

    fun describe() = describe(0)

    abstract val toJson: JsonFlow

    abstract val dependencyFlows: List<UUID>
}
