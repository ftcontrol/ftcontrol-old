package lol.lazar.lazarkit.panels.configurables

import lol.lazar.lazarkit.panels.configurables.annotations.GenericValue
import lol.lazar.lazarkit.panels.data.GenericTypeJson
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
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
        MAP,
        UNKNOWN,
        CUSTOM,
        GENERIC,
        GENERIC_NO_ANNOTATION
    }

    abstract var currentValue: Any?

    abstract val toJsonType: GenericTypeJson

    fun getType(classType: Class<*>?): Types {
        return when (classType) {
            null -> Types.UNKNOWN
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
                if (Map::class.java.isAssignableFrom(classType)) {
                    return Types.MAP
                }
                if (Configurables.customTypeClasses.any { it.className == classType.name }) {
                    return Types.CUSTOM
                }

                val genericType = reference.genericType

                println("DASH: TYPES: Getting type for ${reference.name} of $classType with $currentValue")

                if (genericType is java.lang.reflect.ParameterizedType || genericType is java.lang.reflect.TypeVariable<*>) {
                    val genericAnnotation =
                        parentReference?.reference?.getAnnotation(GenericValue::class.java)
                    if (genericAnnotation != null) {
                        println("   DASH: TYPES: tParam: ${genericAnnotation.tParam}, vParam: ${genericAnnotation.vParam}")

                        when (genericType) {
                            is java.lang.reflect.ParameterizedType -> {
                                println("   DASH: TYPES: Parameterized type")
                                val typeArguments = genericType.actualTypeArguments
                                println("   DASH: TYPES: Actual type arguments: ${typeArguments.contentToString()}")

                                typeArguments.forEach {
                                    println("   DASH: TYPES: Type argument: $it")
                                }
                            }

                            is java.lang.reflect.TypeVariable<*> -> {
                                println("   DASH: TYPES: Type variable")
                                println("   DASH: TYPES: Generic declaration: ${genericType.genericDeclaration}")
                                val resolvedType = when (genericType.name) {
                                    "T" -> genericAnnotation.tParam
                                    "V" -> genericAnnotation.vParam
                                    else -> null
                                }
                                println("   DASH: TYPES: Resolved type for ${genericType.name}: $resolvedType")
                                if(resolvedType != null) {
                                    return getType(resolvedType.java)
                                }
                            }
                        }

                        return Types.GENERIC
                    } else {
                        return Types.GENERIC_NO_ANNOTATION
                    }
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