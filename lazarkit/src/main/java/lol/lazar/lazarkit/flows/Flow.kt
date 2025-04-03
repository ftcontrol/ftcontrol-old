package lol.lazar.lazarkit.flows

open class Flow(
    private val action: suspend () -> Unit,
) {
    var description: String = "Flow"

    open suspend fun execute() = action()

    open fun describe(indent: Int = 0): String =
        "${"  ".repeat(indent)}$description"
}