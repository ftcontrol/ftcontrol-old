package lol.lazar.lazarkit.flows

import kotlinx.coroutines.coroutineScope

open class Flow(
    val action: suspend () -> Unit,
    var description: String
) {
    constructor(action: suspend () -> Unit)
            : this(action = { action() }, description = "Flow")

    open suspend fun execute() = action()

    open fun describe(indent: Int): String =
        "${"  ".repeat(indent)}$description"

    fun describe() = describe(0)
}