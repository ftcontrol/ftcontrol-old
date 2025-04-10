package com.bylazar.bytebox.flows

import com.bylazar.bytebox.panels.json.JsonFlow
import java.util.UUID

class LifecycleFlow(
    val init: (() -> Unit)? = null,
    val loop: () -> Unit,
    val end: (() -> Unit)? = null,
    val runWhile: () -> Boolean = { true },
) : Flow() {

    private enum class State { NOT_STARTED, INIT, LOOP, END, FINISHED }

    private var state: State = State.NOT_STARTED

    override val dependencyFlows: List<UUID>
        get() = TODO("Not yet implemented")

    override val toJson: JsonFlow
        get() = TODO("Not yet implemented")

    override fun innerAction() {
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
