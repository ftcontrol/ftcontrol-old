package lol.lazar.lazarkit.panels.configurables

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.lazar.lazarkit.panels.GlobalData

@Serializable
@SerialName("jvmField")
class GenericTypeJson(
    val className: String? = null,
    val fieldName: String,
    val type: GenericType.Types,
    val valueString: String,
    val possibleValues: List<String>? = null,
    val customValues: List<GenericTypeJson>? = null,
    val arrayValues: List<GenericTypeJson>? = null
) {
    fun toReference(): GenericType? {
        return GlobalData.jvmFields.find { it.className == className && it.name == fieldName }
    }

    val valueAsType: Any
        get() {
            val value = valueString
            return when (type) {
                GenericType.Types.INT -> {
                    when {
                        value.toIntOrNull() != null -> value.toInt()
                        value.toFloatOrNull() != null -> value.toFloat()
                            .toInt()

                        value.toDoubleOrNull() != null -> value.toDouble()
                            .toInt()

                        else -> value.toInt()
                    }
                }

                GenericType.Types.DOUBLE -> {
                    when {
                        value.toDoubleOrNull() != null -> value.toDouble()
                        value.toFloatOrNull() != null -> value.toFloat()
                            .toDouble()

                        else -> value.toDouble()
                    }
                }

                GenericType.Types.STRING -> TODO()
                GenericType.Types.BOOLEAN -> TODO()
                GenericType.Types.FLOAT -> {
                    when {
                        value.toFloatOrNull() != null -> value.toFloat()
                        value.toDoubleOrNull() != null -> value.toDouble()
                            .toFloat()

                        else -> value.toFloat()
                    }
                }

                GenericType.Types.LONG -> {
                    when {
                        value.toLongOrNull() != null -> value.toLong()
                        value.toDoubleOrNull() != null -> value.toDouble()
                            .toLong()

                        else -> value.toLong()
                    }
                }

                GenericType.Types.ENUM -> TODO()
                GenericType.Types.ARRAY -> TODO()
                GenericType.Types.UNKNOWN -> TODO()
                GenericType.Types.CUSTOM -> TODO()
            }
        }
}