package org.firstinspires.ftc.teamcode

import lol.lazar.lazarkit.panels.configurables.TPair
import lol.lazar.lazarkit.panels.configurables.annotations.Configurable

@Configurable
object ClawConfig {
    enum class State {
        OPENED,
        CLOSED
    }

    @JvmField
    var state = State.OPENED

    @JvmField
    var value = TPair({ state }) {
        pair(State.OPENED, 1.0)
        pair(State.CLOSED, 0.0)
        default(0.5)
    }
}