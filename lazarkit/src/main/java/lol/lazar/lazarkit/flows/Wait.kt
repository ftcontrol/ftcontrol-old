package lol.lazar.lazarkit.flows


fun wait(durationMillis: Long) = Wait(durationMillis)

class Wait(val durationMillis: Long) : Flow({}) {
    var startedTimestamp = -1L
    override fun innerAction() {
        if (startedTimestamp == -1L) {
            startedTimestamp = System.currentTimeMillis()
        }

        if (System.currentTimeMillis() - startedTimestamp >= durationMillis) {
            finishedTime = System.currentTimeMillis()
        }
    }
}