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
        ARRAY,
        UNKNOWN,
        CUSTOM
    }

    fun getType(classType: Class<*>?): Types {
        if (classType == null) {
            return Types.UNKNOWN
        }
        return when (classType) {
            Int::class.java, java.lang.Integer::class.java -> Types.INT
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
                Types.UNKNOWN
            }
        }
    }

    val type: Types
        get() = getType(reference.type)

    fun toJsonType(): JsonJvmField {
        if (type == Types.ARRAY) {
            val arrayType = getType(reference.type.componentType)

            val arrayValues: List<String> = when (reference.type) {
                IntArray::class.java -> (currentValue as? IntArray)?.map { it.toString() }
                DoubleArray::class.java -> (currentValue as? DoubleArray)?.map { it.toString() }
                BooleanArray::class.java -> (currentValue as? BooleanArray)?.map { it.toString() }
                FloatArray::class.java -> (currentValue as? FloatArray)?.map { it.toString() }
                LongArray::class.java -> (currentValue as? LongArray)?.map { it.toString() }
                else -> (currentValue as? Array<*>)?.map { it.toString() }
            } ?: emptyList()

            return JsonJvmField(
                className = className,
                fieldName = reference.name,
                type = type,
                arrayType = arrayType,
                currentValueString = arrayValues.joinToString(", "),
                possibleValues = arrayValues
            )
        }

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