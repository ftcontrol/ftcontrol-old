package lol.lazar.lazarkit.flows

fun instant(action: () -> Unit) = Instant(action)

class Instant(action: () -> Unit) : Flow(
    {
        action()
    }
)