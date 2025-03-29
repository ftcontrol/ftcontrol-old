package lol.lazar.lazarkit.panels.configurables

import lol.lazar.lazarkit.panels.GlobalData
import java.lang.reflect.Field

class GenericType(
    var className: String,
    var reference: Field,
    var parentReference: GenericType? = null,
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

    val currentValue: Any?
        get() {
            reference.isAccessible = true
            return try {
                if (parentReference == null) {
                    reference.get(null)
                } else {
                    reference.get(parentReference?.currentValue)
                }
            } catch (e: Exception) {
                println("DASH: Could not get value for ${reference.name}: ${e.message}")
                null
            }
        }

    val name: String
        get() = reference.name


    fun toJsonType(): GenericTypeJson {
        var possibleValues: List<String>? = null
        var customValues: List<GenericTypeJson>? = null

        if (type == Types.ENUM) {
            possibleValues = reference.type.enumConstants.map { it.toString() }
        }

        if (type == Types.CUSTOM) {
            customValues = reference.type.declaredFields.map { field ->
                GenericType(
                    className = className,
                    parentReference = this,
                    reference = field
                ).toJsonType()
            }
        }

        return GenericTypeJson(
            className = className,
            fieldName = name,
            type = type,
            valueString = currentValue.toString(),
            possibleValues = possibleValues,
            customValues = customValues
        )
    }
}