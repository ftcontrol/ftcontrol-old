package lol.lazar.lazarkit.panels.configurables.variables

import lol.lazar.lazarkit.panels.configurables.Configurables
import java.util.UUID

abstract class GenericManager(
    val type: GenericVariable.BaseTypes
) {
    val id: String = UUID.randomUUID().toString()
    val name: String = "no name"
    abstract fun getValue(): Any?
    abstract fun setValue(value: Any): Boolean
    fun setValue(value: String): Boolean {
        return setValue(convertValue(value) ?: return false)
    }

    init {
        Configurables.fieldsMap[id] = this
    }

    private fun convertValue(value: String): Any? {
        return when (type) {
            GenericVariable.BaseTypes.INT -> {
                when {
                    value.toIntOrNull() != null -> value.toInt()
                    value.toFloatOrNull() != null -> value.toFloat()
                        .toInt()

                    value.toDoubleOrNull() != null -> value.toDouble()
                        .toInt()

                    else -> value.toInt()
                }
            }

            GenericVariable.BaseTypes.DOUBLE -> {
                when {
                    value.toDoubleOrNull() != null -> value.toDouble()
                    value.toFloatOrNull() != null -> value.toFloat()
                        .toDouble()

                    else -> value.toDouble()
                }
            }

            GenericVariable.BaseTypes.STRING -> {
                value
            }

            GenericVariable.BaseTypes.BOOLEAN -> {
                value.toBoolean()
            }

            GenericVariable.BaseTypes.FLOAT -> {
                when {
                    value.toFloatOrNull() != null -> value.toFloat()
                    value.toDoubleOrNull() != null -> value.toDouble()
                        .toFloat()

                    else -> value.toFloat()
                }
            }

            GenericVariable.BaseTypes.LONG -> {
                when {
                    value.toLongOrNull() != null -> value.toLong()
                    value.toDoubleOrNull() != null -> value.toDouble()
                        .toLong()

                    else -> value.toLong()
                }
            }

            else -> null
        }
    }
}