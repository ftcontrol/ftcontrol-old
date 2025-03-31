package lol.lazar.lazarkit.panels.configurables

import lol.lazar.lazarkit.panels.data.GenericTypeJson
import java.lang.reflect.Field
import java.util.UUID

abstract class BaseGenericField(
    open var className: String,
    open var reference: Field,
    open var parentReference: GenericField? = null,
    open var id: String = UUID.randomUUID().toString()
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

    abstract var currentValue: Any?

    abstract val toJsonType: GenericTypeJson

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

                if (Configurables.customTypeClasses.any { it.className == classType.name }) {
                    return Types.CUSTOM
                }

                Types.UNKNOWN
            }
        }
    }

    abstract val type: Types

    fun convertValue(value: String): Any? {
        return when (type) {
            Types.INT -> {
                when {
                    value.toIntOrNull() != null -> value.toInt()
                    value.toFloatOrNull() != null -> value.toFloat()
                        .toInt()

                    value.toDoubleOrNull() != null -> value.toDouble()
                        .toInt()

                    else -> value.toInt()
                }
            }

            Types.DOUBLE -> {
                when {
                    value.toDoubleOrNull() != null -> value.toDouble()
                    value.toFloatOrNull() != null -> value.toFloat()
                        .toDouble()

                    else -> value.toDouble()
                }
            }

            Types.STRING -> {
                value
            }

            Types.BOOLEAN -> {
                value.toBoolean()
            }

            Types.FLOAT -> {
                when {
                    value.toFloatOrNull() != null -> value.toFloat()
                    value.toDoubleOrNull() != null -> value.toDouble()
                        .toFloat()

                    else -> value.toFloat()
                }
            }

            Types.LONG -> {
                when {
                    value.toLongOrNull() != null -> value.toLong()
                    value.toDoubleOrNull() != null -> value.toDouble()
                        .toLong()

                    else -> value.toLong()
                }
            }

            Types.ENUM -> {
                reference.type.enumConstants.firstOrNull { c ->
                    c.toString() == value
                }
            }

            else -> null
        }
    }
}