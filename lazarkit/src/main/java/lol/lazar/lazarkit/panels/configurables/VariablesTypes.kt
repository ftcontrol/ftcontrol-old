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
    val arrayType: GenericType.Types? = null,
    val currentValueString: String = "",
    val possibleValues: List<String>? = null,
    val customValues: List<JsonJvmField>? = null
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
        ARRAY,
        UNKNOWN,
        CUSTOM
    }

    fun getType(classType: Class<*>?): Types {
        if (classType == null) {
            return Types.UNKNOWN
        }
        return when (classType) {
            Int::class.java, Integer::class.java -> Types.INT
            Double::class.java, java.lang.Double::class.java -> Types.DOUBLE
            String::class.java -> Types.STRING
            Boolean::class.java, java.lang.Boolean::class.java -> Types.BOOLEAN
            Float::class.java, java.lang.Float::class.java -> Types.FLOAT
            Long::class.java, java.lang.Long::class.java -> Types.LONG
            IntArray::class.java, DoubleArray::class.java, BooleanArray::class.java, FloatArray::class.java, LongArray::class.java -> Types.ARRAY
            else -> {
                if (reference.type.isEnum) {
                    return Types.ENUM
                }
                if (reference.type.isArray) {
                    return Types.ARRAY
                }

                if (GlobalData.customTypeClasses.any { it.className == classType.name }) {
                    return Types.CUSTOM
                }

                Types.UNKNOWN
            }
        }
    }

    val type: Types
        get() = getType(reference.type)

    fun toJsonType(): JsonJvmField {
        val currentValue: Any? = runCatching {
            if (reference.isAccessible) reference.get(null) else null
        }.getOrNull()

        return when (type) {
            Types.CUSTOM -> {
                val nestedFields = reference.type.declaredFields.mapNotNull { field ->
                    try {
                        field.isAccessible = true
                        val fieldValue = currentValue?.let { field.get(it) }
                        GenericType(field.declaringClass.name, field, fieldValue).toJsonType()
                    } catch (e: Exception) {
                        null
                    }
                }

                JsonJvmField(
                    className = className,
                    fieldName = reference.name,
                    type = type,
                    customValues = nestedFields.takeIf { it.isNotEmpty() } // Only add if not empty
                )
            }

            Types.ARRAY -> JsonJvmField(
                className = className,
                fieldName = reference.name,
                type = type,
                arrayType = getType(reference.type.componentType),
                possibleValues = getArrayValues(currentValue)
            )

            Types.ENUM -> JsonJvmField(
                className = className,
                fieldName = reference.name,
                type = type,
                currentValueString = currentValue.toString(),
                possibleValues = reference.type.enumConstants.map { it.toString() }
            )

            else -> JsonJvmField(
                className = className,
                fieldName = reference.name,
                type = type,
                currentValueString = currentValue.toString(),
            )
        }
    }

    private fun getArrayValues(value: Any?): List<String> = when (value) {
        is IntArray -> value.map { it.toString() }
        is DoubleArray -> value.map { it.toString() }
        is BooleanArray -> value.map { it.toString() }
        is FloatArray -> value.map { it.toString() }
        is LongArray -> value.map { it.toString() }
        is Array<*> -> value.mapNotNull { it?.toString() }
        else -> emptyList()
    }
}