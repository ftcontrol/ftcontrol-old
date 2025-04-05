package lol.lazar.lazarkit.flows

import kotlinx.coroutines.sync.withLock
import lol.lazar.lazarkit.core.GlobalEntities
import java.util.UUID

open class Flow(
    val action: suspend () -> Unit,
    var description: String,
    var entityId: String? = null,
) {
    constructor(action: suspend () -> Unit)
            : this(action = { action() }, description = "Flow")

    val id = UUID.randomUUID()

    var finishedTime: Long = -1L
    val isFinished: Boolean get() = finishedTime != -1L

    open suspend fun execute() {
        val entity = entityId?.let { GlobalEntities.entities[it] }
        if (entity != null) {
            entity.mutex.withLock {
                action()
            }
        } else {
            action()
        }
        finishedTime = System.currentTimeMillis()
    }

    open fun describe(indent: Int): String =
        "${"  ".repeat(indent)}$description"

    fun describe() = describe(0)
}