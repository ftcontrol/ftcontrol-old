package lol.lazar.lazarkit.flows


open class Flow(
    val execute: suspend () -> Unit,
    val description: String
) {
    open suspend fun execute() {
        execute()
    }

    open fun describe(indent: Int = 0): String {
        val indentString = "  ".repeat(indent)
        return "$indentString$description"
    }
}