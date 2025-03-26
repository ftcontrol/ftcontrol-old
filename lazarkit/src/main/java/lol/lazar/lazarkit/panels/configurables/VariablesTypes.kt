package lol.lazar.lazarkit.panels.configurables

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.lazar.lazarkit.panels.GlobalData
import java.lang.reflect.Field

@Serializable
@SerialName("jvmField")
class JsonJvmField(
    val className: String,
    val fieldName: String,
    val type: GenericType.Types,
    val currentValueString: String,
    val possibleValues: List<String>? = null
) {
    fun toReference(): GenericType? {
        return GlobalData.jvmFields.find { it.className == className && it.reference.name == fieldName }
    }
}

class GenericType(
    val className: String,
    val reference: Field,
    val currentValue: Any?
) {
    enum class Types {
        INT,
        DOUBLE,
        STRING,
        BOOLEAN,
        FLOAT,
        LONG,
        ENUM,
        UNKNOWN,
        CUSTOM
    }

    val type: Types
        get() {
            return when (reference.type) {
                Int::class.java -> Types.INT
                Double::class.java -> Types.DOUBLE
                String::class.java -> Types.STRING
                Boolean::class.java -> Types.BOOLEAN
                Float::class.java -> Types.FLOAT
                Long::class.java -> Types.LONG
                else -> {
                    if(reference.type.isEnum){
                        return Types.ENUM
                    }
                    Types.UNKNOWN
                }
            }
        }

    fun toJsonType(): JsonJvmField {
        val possibleValues: List<String>? = when (type) {
            Types.ENUM -> reference.type.enumConstants.map { it.toString() }
            else -> null
        }
        return JsonJvmField(
            className = className,
            fieldName = reference.name,
            type = type,
            currentValueString = currentValue.toString(),
            possibleValues = possibleValues
        )
    }
}