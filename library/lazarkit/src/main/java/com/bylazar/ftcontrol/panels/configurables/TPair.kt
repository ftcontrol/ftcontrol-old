package com.bylazar.ftcontrol.panels.configurables

import com.bylazar.ftcontrol.panels.configurables.annotations.ConfigurableCustomType
import com.bylazar.ftcontrol.panels.configurables.annotations.IgnoreConfigurable

@ConfigurableCustomType
open class TPair<T, V>(
    @field:IgnoreConfigurable
    var lambdaProvider: () -> T,
    var defaultValue: V? = null,
    var states: MutableMap<T, V> = mutableMapOf(),
    block: TPair<T, V>.() -> Unit
) {

    init {
        this.block()
    }

    fun pair(state: T, value: V) {
        states[state] = value
    }

    fun default(value: V) {
        defaultValue = value
    }

    operator fun invoke(): V {
        return getStateValue(lambdaProvider.invoke())
    }

    fun getStateValue(state: T): V {
        return states[state] ?: defaultValue
        ?: throw NoSuchElementException("State '$state' not found and no default value provided")
    }
}