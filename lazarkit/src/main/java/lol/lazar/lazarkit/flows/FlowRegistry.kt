package lol.lazar.lazarkit.flows

import java.util.UUID

object FlowRegistry {
    private val flows = mutableMapOf<UUID, Flow>()

    val hasFlows: Boolean get() = flows.isNotEmpty()

    fun register(flow: Flow) {
        flows[flow.id] = flow
    }

    fun unregister(id: UUID) {
        flows.remove(id)
    }

    fun unregister(flow: Flow) = unregister(flow.id)

    fun get(id: UUID): Flow? = flows[id]

    fun execute(id: UUID) {
        get(id)?.execute()
    }

    fun execute(flow: Flow) = execute(flow.id)

    fun init() {
        flows.clear()
    }

    fun loop() {
        flows.values.forEach {
            if (it.isFinished) flows.remove(it.id)
        }
        flows.values.forEach { it.execute() }
    }
}