package lol.lazar.lazarkit.flows

class LifecycleFlow(
    val init: (() -> Unit)? = null,
    val loop: () -> Unit,
    val end: (() -> Unit)? = null,
    val runWhile: () -> Boolean = { true },
    description: String = "LifecycleFlow",
    entityId: String? = null,
) : Flow(action = {}, description = description, entityId = entityId) {

    private enum class State { NOT_STARTED, INIT, LOOP, END, FINISHED }

    private var state: State = State.NOT_STARTED

    init {
        action = {
            when (state) {
                State.NOT_STARTED -> {
                    state = State.INIT
                }

                State.INIT -> {
                    init?.invoke()
                    state = State.LOOP
                }

                State.LOOP -> {
                    if (runWhile()) {
                        loop()
                    } else {
                        state = State.END
                    }
                }

                State.END -> {
                    end?.invoke()
                    state = State.FINISHED
                    finishedTime = System.currentTimeMillis()
                }

                State.FINISHED -> {
                }
            }
        }
    }
}
