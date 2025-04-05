package lol.lazar.lazarkit.core

import java.util.UUID

class Entity {
    var id = UUID.randomUUID().toString()

    var runningFlowId: UUID? = null
    val isBusy: Boolean get() = runningFlowId != null

    fun lock(flowId: UUID) {
        runningFlowId = flowId
    }

    fun unlock() {
        runningFlowId = null
    }

    init {
        GlobalEntities.entities[id] = this
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Entity -> id == other.id
            else -> false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}