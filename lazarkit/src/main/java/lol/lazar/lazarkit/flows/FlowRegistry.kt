package lol.lazar.lazarkit.flows

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

object FlowRegistry {
    private val flows = mutableMapOf<UUID, Flow>()
    private val mutex = Mutex()

    suspend fun register(flow: Flow) {
        mutex.withLock {
            flows[flow.id] = flow
        }
    }

    suspend fun unregister(id: UUID) {
        mutex.withLock {
            flows.remove(id)
        }
    }

    suspend fun unregister(flow: Flow) = unregister(flow.id)

    suspend fun get(id: UUID): Flow? = mutex.withLock { flows[id] }

    suspend fun execute(id: UUID) {
        get(id)?.execute()
    }

    suspend fun execute(flow: Flow) = execute(flow.id)

    suspend fun init() {
        mutex.withLock { flows.clear() }
    }

    suspend fun loop() {
        mutex.withLock {
            flows.values.forEach {
                if (it.isFinished) flows.remove(it.id)
            }
            flows.values.forEach { it.execute() }
        }
    }
}