package lol.lazar.lazarkit.flows

import kotlinx.coroutines.sync.withLock
import lol.lazar.lazarkit.core.GlobalEntities

open class Flow(
    val action: suspend () -> Unit,
    var description: String,
    var entityId: String? = null,
) {
    constructor(action: suspend () -> Unit)
            : this(action = { action() }, description = "Flow")

    open suspend fun execute() {
        val entity = entityId?.let { GlobalEntities.entities[it] }
        if (entity != null) {
            entity.mutex.withLock {
                action()
            }
        } else {
            action()
        }
    }

    open fun describe(indent: Int): String =
        "${"  ".repeat(indent)}$description"

    fun describe() = describe(0)
}