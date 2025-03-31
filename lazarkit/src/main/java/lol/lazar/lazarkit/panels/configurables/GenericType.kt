package lol.lazar.lazarkit.panels.configurables

import java.lang.reflect.Array
import java.lang.reflect.Field
import java.util.UUID

class GenericType(
    var className: String,
    var reference: Field,
    var parentReference: GenericType? = null,
    var id: String = UUID.randomUUID().toString()
) {
//    TODO: optimize using lazy
//    TODO: decorator for quick values to select from

    var customValues: List<GenericType>? = null

    init {
        GlobalFields.fieldsMap[id] = this

        if (type == Types.CUSTOM) {
            customValues = reference.type.declaredFields.map { field ->
                GenericType(
                    className = className,
                    parentReference = this,
                    reference = field
                )
            }
        }
    }

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

                if (GlobalFields.customTypeClasses.any { it.className == classType.name }) {
                    return Types.CUSTOM
                }

                Types.UNKNOWN
            }
        }
    }

    val type: Types
        get() = getType(reference.type)

    var currentValue: Any?
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
        set(value) {
            reference.isAccessible = true

            try {
                if (parentReference == null) {
                    reference.set(null, value)
                } else {
                    reference.set(parentReference?.currentValue, value)
                    println("DASH: Set ${reference.name} to $value")
                }
            } catch (e: Exception) {
                println("DASH: Could not set value for ${reference.name}: ${e.message}")
            }
        }

    val name: String
        get() = reference.name

    val possibleValues: List<String>? by lazy {
        if (type == Types.ENUM) {
            reference.type.enumConstants.map { it.toString() }
        } else {
            null
        }
    }

    fun toJsonType(): GenericTypeJson {
        var arrayValues: List<GenericTypeJson>? = null

        if (type == Types.ARRAY) {
            val componentType = reference.type.componentType;
            val componentValues = currentValue;
            if (componentValues != null && componentType != null) {
                arrayValues = (0 until Array.getLength(componentValues)).map { i ->
                    val element = Array.get(componentValues, i);
                    GenericTypeJson(
                        id = id,
                        className = componentType.simpleName,
                        fieldName = "[${i}]",
                        type = getType(element.javaClass),
                        valueString = element?.toString() ?: "",
                        newValueString = element?.toString() ?: "",
                    );
                };
            }
        }

        return GenericTypeJson(
            id = id,
            className = className,
            fieldName = name,
            type = type,
            valueString = currentValue.toString(),
            newValueString = currentValue.toString(),
            possibleValues = possibleValues,
            customValues = customValues?.map { it.toJsonType() },
            arrayValues = arrayValues
        )
    }

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

            Types.ARRAY -> null
            Types.UNKNOWN -> null
            Types.CUSTOM -> null
        }
    }
}