package lol.lazar.lazarkit.panels.configurables.variables.generics

import lol.lazar.lazarkit.panels.configurables.Configurables
import lol.lazar.lazarkit.panels.configurables.variables.BaseTypes
import java.util.UUID

class GenericManager(
    val type: BaseTypes,
    val getValue: () -> Any?,
    val setValue: (value: Any) -> Boolean,
    val possibleValues: List<String>? = null
) {
    val id: String = UUID.randomUUID().toString()
    val name: String = "no name"
    fun setValue(value: String): Boolean {
        return setValue(convertValue(value) ?: return false)
    }

    init {
        Configurables.fieldsMap[id] = this
    }

    private fun convertValue(value: String): Any? {
        return when (type) {
            BaseTypes.INT -> {
                when {
                    value.toIntOrNull() != null -> value.toInt()
                    value.toFloatOrNull() != null -> value.toFloat()
                        .toInt()

                    value.toDoubleOrNull() != null -> value.toDouble()
                        .toInt()

                    else -> value.toInt()
                }
            }

            BaseTypes.DOUBLE -> {
                when {
                    value.toDoubleOrNull() != null -> value.toDouble()
                    value.toFloatOrNull() != null -> value.toFloat()
                        .toDouble()

                    else -> value.toDouble()
                }
            }

            BaseTypes.STRING -> {
                value
            }

            BaseTypes.BOOLEAN -> {
                value.toBoolean()
            }

            BaseTypes.FLOAT -> {
                when {
                    value.toFloatOrNull() != null -> value.toFloat()
                    value.toDoubleOrNull() != null -> value.toDouble()
                        .toFloat()

                    else -> value.toFloat()
                }
            }

            BaseTypes.LONG -> {
                when {
                    value.toLongOrNull() != null -> value.toLong()
                    value.toDoubleOrNull() != null -> value.toDouble()
                        .toLong()

                    else -> value.toLong()
                }
            }

            BaseTypes.ENUM -> {
                if (possibleValues == null) return null
                for (enumValue in possibleValues) {
                    if (enumValue == value) return enumValue
                }
                return null
            }

            else -> null
        }
    }
}