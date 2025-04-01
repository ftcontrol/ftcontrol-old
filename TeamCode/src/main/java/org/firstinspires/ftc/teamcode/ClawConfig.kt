package org.firstinspires.ftc.teamcode

import lol.lazar.lazarkit.panels.configurables.TPair
import lol.lazar.lazarkit.panels.configurables.annotations.Configurable
import lol.lazar.lazarkit.panels.configurables.annotations.GenericValue

@Configurable
object ClawConfig {
    enum class State {
        OPENED,
        CLOSED
    }

    @JvmField
    @field:GenericValue(State::class, Double::class)
    var state = State.OPENED

    @JvmField
    @field:GenericValue(State::class, Double::class)
    var testTParam = TPair({ state }) {
        pair(State.OPENED, 1.0)
        pair(State.CLOSED, 0.0)
        default(0.5)
    }
}