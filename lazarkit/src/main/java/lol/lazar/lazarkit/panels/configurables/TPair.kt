package lol.lazar.lazarkit.panels.configurables

import lol.lazar.lazarkit.panels.configurables.annotations.ConfigurableCustomType
import lol.lazar.lazarkit.panels.configurables.annotations.IgnoreConfigurable

@ConfigurableCustomType
open class TPair<T, V>(
    @field:IgnoreConfigurable
    open var lambdaProvider: () -> T,
    open var defaultValue: V? = null,
    open var states: MutableMap<T, V> = mutableMapOf(),
    block: TPair<T, V>.() -> Unit
) {

    init {
        this.block()
    }

    fun pair(state: T, value: V) {
        states[state] = value
    }

    operator fun invoke(): V {
        return getStateValue(lambdaProvider.invoke())
    }

    fun getStateValue(state: T): V {
        return states[state] ?: defaultValue
        ?: throw NoSuchElementException("State '$state' not found and no default value provided")
    }
}

@ConfigurableCustomType
class DoublePair<T>(
    @field:IgnoreConfigurable
    override var lambdaProvider: () -> T,
    override var defaultValue: Double? = null,
    override var states: MutableMap<T, Double> = mutableMapOf(),
    block: TPair<T, Double>.() -> Unit
) : TPair<T, Double>(lambdaProvider, defaultValue, states, block = block)