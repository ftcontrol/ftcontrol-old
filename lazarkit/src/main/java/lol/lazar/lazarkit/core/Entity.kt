package lol.lazar.lazarkit.core

import kotlinx.coroutines.sync.Mutex
import java.util.UUID

class Entity {
    var id = UUID.randomUUID().toString()

    val mutex = Mutex()
    val isBudy
        get() = mutex.isLocked

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