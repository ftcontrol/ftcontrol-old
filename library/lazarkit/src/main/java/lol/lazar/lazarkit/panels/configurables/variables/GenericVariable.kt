package lol.lazar.lazarkit.panels.configurables.variables

import lol.lazar.lazarkit.panels.configurables.Configurables
import lol.lazar.lazarkit.panels.configurables.annotations.GenericValue
import lol.lazar.lazarkit.panels.json.GenericTypeJson
import java.lang.reflect.ParameterizedType

fun getType(
    classType: Class<*>?,
    reference: MyField? = null,
    parentReference: MyField? = null
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

            val genericType = reference.field?.genericType

            if (genericType is ParameterizedType || genericType is java.lang.reflect.TypeVariable<*>) {
                val genericAnnotation =
                    parentReference.field?.getAnnotation(GenericValue::class.java)
                if (genericAnnotation != null) {

                    when (genericType) {
                        is java.lang.reflect.TypeVariable<*> -> {
                            val resolvedType = when (genericType.name) {
                                "T" -> genericAnnotation.tParam
                                "V" -> genericAnnotation.vParam
                                else -> null
                            }
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



abstract class GenericVariable(
    open val reference: MyField?,
    open val className: String,
) {
    abstract val toJsonType: GenericTypeJson
}

abstract class GenericManagedVariable(
    override val reference: MyField?,
    override val className: String,
) : GenericVariable(reference, className) {
    abstract val manager: GenericManager
}