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

    override fun toString(): String {
        return "JsonJvmField(className='$className', fieldName='$fieldName', type=$type, arrayType=$arrayType, currentValueString='$currentValueString', possibleValues=$possibleValues, customValues=$customValues)"
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
            println("DASH: Type is null")
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
                if (classType.isEnum) {
                    return Types.ENUM
                }
                if (classType.isArray) {
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

    override fun toString(): String {
        return "GenericType(className='$className', reference=$reference, currentValue=$currentValue)"
    }

    fun toJsonType(): JsonJvmField {
//        val currentValue: Any? = runCatching {
//            if (reference.isAccessible) reference.get(null) else null
//        }.getOrNull()

        return when (type) {
            Types.CUSTOM -> {
                try {
                    println("DASH: ")
                    println("DASH: Found custom FIELD: ${reference.name} ${reference.type.name}")
                    val nestedFields = reference.type.declaredFields.map {
                        println("   DASH: Custom type field: ${it.name}")
                        println("   DASH: Custom type field type: ${it.type}")
                        println("   DASH: Custom type fields my types: ${getType(it.type)}")
                        println("   DASH: Custom type field value: ${it.get(currentValue)}")

                        val value = it.get(currentValue)

                        val genericAnswer =
                            GenericType(it.declaringClass.name, it, value)
                        println("   DASH: Custom type field struct: $genericAnswer")
                        println("   DASH: Custom type field json: ${genericAnswer.toJsonType()}")
                        println("   DASH: ")

                        genericAnswer.toJsonType()
                    }

                    JsonJvmField(
                        className = className,
                        fieldName = reference.name,
                        type = type,
                        customValues = nestedFields
                    )
                } catch (e: Exception) {
                    println("DASH: Error getting custom type: ${e.message}")
                    e.printStackTrace()
                    JsonJvmField(
                        className = className,
                        fieldName = reference.name,
                        type = type,
                    )
                }
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