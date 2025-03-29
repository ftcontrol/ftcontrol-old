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
    val index: Int? = null,
    val arrayType: GenericType.Types? = null,
    val currentValueString: String = "",
    val possibleValues: List<String>? = null,
    val customValues: List<JsonJvmField>? = null,
    val arrayValues: List<JsonJvmField>? = null
) {
    fun toReference(): GenericType? {
        return GlobalData.jvmFields.find { it.className == className && it.reference.name == fieldName }
    }

    override fun toString(): String {
        return "JsonJvmField(className='$className', fieldName='$fieldName', type=$type, arrayType=$arrayType, currentValueString='$currentValueString', possibleValues=$possibleValues, customValues=$customValues)"
    }
}

@Serializable
@SerialName("jvmField")
class ArrayItem(
    val index: Int,
    val value: String,
    val type: GenericType.Types
)

class GenericType(
    val className: String,
    val reference: Field,
    val currentValue: Any?,
    val index: Int? = null,
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
        return when (type) {
            Types.CUSTOM -> {
                try {
                    JsonJvmField(
                        className = className,
                        fieldName = reference.name,
                        type = type,
                        customValues = reference.type.declaredFields.map {
//                            TODO: get index here
                            GenericType(
                                it.declaringClass.name,
                                it,
                                it.get(currentValue),
                                index = index
                            ).toJsonType()
                        },
                        index = index
                    )
                } catch (e: Exception) {
                    println("DASH: Error getting custom type: ${e.message}")
                    e.printStackTrace()
                    JsonJvmField(
                        className = className,
                        fieldName = reference.name,
                        type = type,
                        index = index
                    )
                }
            }

            Types.ARRAY -> {
                val innerType = getType(reference.type.componentType)

                println("DASH: Found an array of name: ${reference.name}")
                println("DASH: Found an array of type: ${reference.type.componentType}")
                println("DASH: Found an array of my type: $innerType")
                println("DASH: Current value: $currentValue")

                val jsonFields = mutableListOf<JsonJvmField>()

                if (innerType == Types.CUSTOM) {
                    val customClass = reference.type.componentType
                    println("DASH: Processing custom type array: ${customClass.name}")

                    (currentValue as? Array<*>)?.forEachIndexed { i, value ->
                        if (value != null) {
                            println("   DASH: Processing element at index $i: ${value.javaClass.name}")

                            val fields = value.javaClass.declaredFields
                            val fieldData = fields.map { field ->
                                field.isAccessible = true
                                val fieldValue = field.get(value)
                                GenericType(
                                    field.declaringClass.name,
                                    field,
                                    fieldValue,
                                    index = i
                                ).toJsonType()
//                                JsonJvmField(
//                                    className = field.declaringClass.name,
//                                    fieldName = field.name,
//                                    type = getType(field.type),
//                                    arrayType = null,
//                                    possibleValues = listOf(fieldValue.toString()),
//                                    index = i
//                                ).also {
//                                    println("   DASH: Extracted field: ${field.name} = $fieldValue")
//                                }
                            }

                            jsonFields.addAll(fieldData)
                        }
                    }
                } else if (innerType == Types.UNKNOWN) {
                    (currentValue as? Array<*>)?.forEachIndexed { i, value ->
                        println("   DASH: index: $i -> [$value] (${value?.javaClass?.name ?: "null"})")

                        val jsonValue = GenericType(
                            className = className,
                            reference = reference,
                            currentValue = value,
                            index = i
                        ).toJsonType()

                        jsonFields.add(jsonValue)
                    }
                } else {
                    when (currentValue) {
                        is IntArray -> currentValue.forEachIndexed { i, value ->
                            jsonFields.add(
                                JsonJvmField(
                                    className = className,
                                    fieldName = reference.name,
                                    type = type,
                                    arrayType = innerType,
                                    possibleValues = listOf(value.toString()),
                                    index = i
                                )
                            )
                        }

                        is DoubleArray -> currentValue.forEachIndexed { i, value ->
                            jsonFields.add(
                                JsonJvmField(
                                    className = className,
                                    fieldName = reference.name,
                                    type = type,
                                    arrayType = innerType,
                                    possibleValues = listOf(value.toString()),
                                    index = i
                                )
                            )
                        }

                        is BooleanArray -> currentValue.forEachIndexed { i, value ->
                            jsonFields.add(
                                JsonJvmField(
                                    className = className,
                                    fieldName = reference.name,
                                    type = type,
                                    arrayType = innerType,
                                    possibleValues = listOf(value.toString()),
                                    index = i
                                )
                            )
                        }

                        is FloatArray -> currentValue.forEachIndexed { i, value ->
                            jsonFields.add(
                                JsonJvmField(
                                    className = className,
                                    fieldName = reference.name,
                                    type = type,
                                    arrayType = innerType,
                                    possibleValues = listOf(value.toString()),
                                    index = i
                                )
                            )
                        }

                        is LongArray -> currentValue.forEachIndexed { i, value ->
                            jsonFields.add(
                                JsonJvmField(
                                    className = className,
                                    fieldName = reference.name,
                                    type = type,
                                    arrayType = innerType,
                                    possibleValues = listOf(value.toString()),
                                    index = i
                                )
                            )
                        }

                        is Array<*> -> currentValue.forEachIndexed { i, value ->
                            jsonFields.add(
                                JsonJvmField(
                                    className = className,
                                    fieldName = reference.name,
                                    type = type,
                                    arrayType = innerType,
                                    possibleValues = listOf(value.toString()),
                                    index = i
                                )
                            )
                        }

                        else -> {}
                    }
                }

                return JsonJvmField(
                    className = className,
                    fieldName = reference.name,
                    type = type,
                    arrayType = innerType,
                    arrayValues = jsonFields,
                    index = index
                ).also { println("DASH: Final JSON Field: $it") }
            }

            Types.ENUM -> JsonJvmField(
                className = className,
                fieldName = reference.name,
                type = type,
                currentValueString = currentValue.toString(),
                possibleValues = reference.type.enumConstants.map { it.toString() },
                index = index
            )

            else -> JsonJvmField(
                className = className,
                fieldName = reference.name,
                type = type,
                currentValueString = currentValue.toString(),
                index = index
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