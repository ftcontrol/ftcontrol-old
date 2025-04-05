package lol.lazar.lazarkit.flows

fun instant(action: () -> Unit) = Instant(action)

open class Instant(override var action: () -> Unit) : Flow({}) {
    init {
        println("instant id: $id")
    }
    override fun innerAction() {
        println("   Running instant")
        action()
        finishedTime = System.currentTimeMillis()
    }
}