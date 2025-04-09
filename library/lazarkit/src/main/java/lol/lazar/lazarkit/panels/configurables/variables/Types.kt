package lol.lazar.lazarkit.panels.configurables.variables

import lol.lazar.lazarkit.panels.configurables.Configurables
import lol.lazar.lazarkit.panels.configurables.annotations.GenericValue
import java.lang.reflect.ParameterizedType

private val typeMapping = mapOf(
    Int::class.java to BaseTypes.INT,
    Integer::class.java to BaseTypes.INT,
    Double::class.java to BaseTypes.DOUBLE,
    java.lang.Double::class.java to BaseTypes.DOUBLE,
    String::class.java to BaseTypes.STRING,
    Boolean::class.java to BaseTypes.BOOLEAN,
    java.lang.Boolean::class.java to BaseTypes.BOOLEAN,
    Float::class.java to BaseTypes.FLOAT,
    java.lang.Float::class.java to BaseTypes.FLOAT,
    Long::class.java to BaseTypes.LONG,
    java.lang.Long::class.java to BaseTypes.LONG,
    IntArray::class.java to BaseTypes.ARRAY,
    DoubleArray::class.java to BaseTypes.ARRAY,
    BooleanArray::class.java to BaseTypes.ARRAY,
    FloatArray::class.java to BaseTypes.ARRAY,
    LongArray::class.java to BaseTypes.ARRAY,
)

fun getType(
    classType: Class<*>?,
    reference: MyField? = null,
    parentReference: MyField? = null
): BaseTypes {
    return when {
        classType == null -> BaseTypes.UNKNOWN
        typeMapping.containsKey(classType) -> typeMapping[classType] ?: BaseTypes.UNKNOWN
        classType.isEnum -> BaseTypes.ENUM
        classType.isArray -> BaseTypes.ARRAY
        Map::class.java.isAssignableFrom(classType) -> BaseTypes.MAP
        List::class.java.isAssignableFrom(classType) -> BaseTypes.LIST
        MutableList::class.java.isAssignableFrom(classType) -> BaseTypes.LIST

        Configurables.customTypeClasses.any { it.className == classType.name } -> BaseTypes.CUSTOM
        else -> resolveGenericType(reference, parentReference)
    }
}

private fun resolveGenericType(reference: MyField?, parentReference: MyField?): BaseTypes {
    if (reference == null || parentReference == null) return BaseTypes.UNKNOWN

    val genericType = reference.ref?.genericType

    val isGeneric =
        genericType is ParameterizedType || genericType is java.lang.reflect.TypeVariable<*>

    if (!isGeneric) return BaseTypes.UNKNOWN

    val genericAnnotation = parentReference.ref?.getAnnotation(GenericValue::class.java)

    if (genericAnnotation == null) return BaseTypes.GENERIC_NO_ANNOTATION

    if (genericType is ParameterizedType) return BaseTypes.GENERIC

    if (genericType is java.lang.reflect.TypeVariable<*>) {
        val resolvedType = when (genericType.name) {
            "T" -> genericAnnotation.tParam
            "V" -> genericAnnotation.vParam
            else -> null
        }
        if (resolvedType != null) {
            return getType(resolvedType.java)
        }
    }

    return BaseTypes.GENERIC
}