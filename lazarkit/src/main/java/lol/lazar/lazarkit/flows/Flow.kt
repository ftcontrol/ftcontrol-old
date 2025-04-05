package lol.lazar.lazarkit.flows

import lol.lazar.lazarkit.core.GlobalEntities
import java.util.UUID

open class Flow(
    open var action: () -> Unit,
    var description: String,
    var entityId: String? = null,
) {
    constructor(action: () -> Unit)
            : this(action = { action() }, description = "Flow")

    open fun innerAction() {
        action()
    }

    val id = UUID.randomUUID()

    var finishedTime: Long = -1L
    val isFinished: Boolean get() = finishedTime != -1L

    open fun execute() {
        if (isFinished) return
        val entity = entityId?.let { GlobalEntities.entities[it] }
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
}
