package lol.lazar.lazarkit.panels.configurables.variables

import lol.lazar.lazarkit.panels.configurables.Configurables
import lol.lazar.lazarkit.panels.configurables.annotations.GenericValue
import lol.lazar.lazarkit.panels.json.GenericTypeJson
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

fun getType(
    classType: Class<*>?,
    reference: Field? = null,
    parentReference: Field? = null
): BaseTypes {
    return when (classType) {
        null -> BaseTypes.UNKNOWN
        Int::class.java, Integer::class.java -> BaseTypes.INT
        Double::class.java, java.lang.Double::class.java -> BaseTypes.DOUBLE
        String::class.java -> BaseTypes.STRING
        Boolean::class.java, java.lang.Boolean::class.java -> BaseTypes.BOOLEAN
        Float::class.java, java.lang.Float::class.java -> BaseTypes.FLOAT
        Long::class.java, java.lang.Long::class.java -> BaseTypes.LONG
        IntArray::class.java, DoubleArray::class.java, BooleanArray::class.java, FloatArray::class.java, LongArray::class.java -> BaseTypes.ARRAY
        else -> {
            if (classType.isEnum) {
                return BaseTypes.ENUM
            }
            if (classType.isArray) {
                return BaseTypes.ARRAY
            }
            if (Map::class.java.isAssignableFrom(classType)) {
                return BaseTypes.MAP
            }
            if (List::class.java.isAssignableFrom(classType) ||
                MutableList::class.java.isAssignableFrom(classType)
            ) {
                return BaseTypes.LIST
            }
            if (Configurables.customTypeClasses.any { it.className == classType.name }) {
                return BaseTypes.CUSTOM
            }

            if (reference == null || parentReference == null) return BaseTypes.UNKNOWN

            val genericType = reference.genericType

            println("DASH: BaseTypes: Getting type for ${reference.name} of $classType")

            if (genericType is ParameterizedType || genericType is java.lang.reflect.TypeVariable<*>) {
                val genericAnnotation =
                    parentReference.getAnnotation(GenericValue::class.java)
                if (genericAnnotation != null) {
                    println("   DASH: TYPES: tParam: ${genericAnnotation.tParam}, vParam: ${genericAnnotation.vParam}")

                    when (genericType) {
                        is ParameterizedType -> {
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
                            if (resolvedType != null) {
                                return getType(resolvedType.java)
                            }
                        }
                    }

                    return BaseTypes.GENERIC
                } else {
                    return BaseTypes.GENERIC_NO_ANNOTATION
                }
            }

            BaseTypes.UNKNOWN
        }
    }
}

enum class BaseTypes {
    INT,
    LONG,
    DOUBLE,
    STRING,
    BOOLEAN,
    FLOAT,
    ENUM,

    UNKNOWN,

    CUSTOM,
    ARRAY,
    LIST,
    MAP,
    GENERIC,
    GENERIC_NO_ANNOTATION
}

abstract class GenericVariable(
    open val reference: Field?,
    open val className: String,
) {
    abstract val manager: GenericManager

    abstract val toJsonType: GenericTypeJson
}