package lol.lazar.lazarkit.panels.configurables.variables

import lol.lazar.lazarkit.panels.configurables.annotations.IgnoreConfigurable
import lol.lazar.lazarkit.panels.json.GenericTypeJson
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.lang.reflect.Type

class UnknownVariable(
    override val className: String,
    val name: String
) : GenericVariable(null, className) {
    override val toJsonType: GenericTypeJson
        get() = GenericTypeJson(
            id = "",
            className = className,
            fieldName = name,
            type = BaseTypes.UNKNOWN,
            valueString = "",
            newValueString = "",
        )
}

class SimpleVariable(
    override val reference: MyField,
    override val className: String
) : GenericManagedVariable(reference, className) {
    val type = getType(reference.type, reference, null)

    override val manager = GenericManager(
        type,
        getValue = {
            reference.isAccessible = true
            try {
                reference.get(null)
            } catch (e: Exception) {
                println("DASH: Could not get value for ${reference.name}: ${e.message}")
                null
            }
        },
        setValue = {
            reference.isAccessible = true
            try {
                reference.set(null, it)
                true
            } catch (e: Exception) {
                println("DASH: Could not set value for ${reference.name}: ${e.message}")
                false
            }
        },
        possibleValues = when (type) {
            BaseTypes.BOOLEAN -> listOf("true", "false")
            BaseTypes.ENUM -> reference.type.enumConstants.map { it.toString() }
            else -> null
        }
    )

    override val toJsonType: GenericTypeJson
        get() {
            val value = manager.getValue()?.toString() ?: ""
            return GenericTypeJson(
                id = manager.id,
                className = className,
                fieldName = reference.name,
                type = type,
                valueString = value,
                newValueString = value,
                possibleValues = manager.possibleValues
            )
        }
}

class NestedVariable(
    override val reference: MyField,
    val parentReference: GenericManagedVariable,
    override val className: String
) : GenericManagedVariable(reference, className) {
    val type = getType(reference.type, reference, parentReference.reference)

    override val manager = GenericManager(
        type,
        getValue = {
            reference.isAccessible = true
            try {
                reference.get(parentReference.manager.getValue())
            } catch (e: Exception) {
                println("DASH: Could not get value for ${reference.name}: ${e.message}")
                null
            }
        },
        setValue = {
            reference.isAccessible = true
            try {
                reference.set(parentReference.manager.getValue(), it)
                true
            } catch (e: Exception) {
                println("DASH: Could not set value for ${reference.name}: ${e.message}")
                false
            }
        },
        possibleValues = when (type) {
            BaseTypes.BOOLEAN -> listOf("true", "false")
            BaseTypes.ENUM -> reference.type.enumConstants.map { it.toString() }
            else -> null
        }
    )
    override val toJsonType: GenericTypeJson
        get() {
            val value = manager.getValue()?.toString() ?: ""
            return GenericTypeJson(
                id = manager.id,
                className = className,
                fieldName = reference.name,
                type = type,
                valueString = value,
                newValueString = value,
                possibleValues = manager.possibleValues
            )
        }
}

class CustomVariable(
    val fieldName: String,
    override val className: String,
    val values: List<GenericVariable>,
    val type: BaseTypes = BaseTypes.CUSTOM
) : GenericVariable(null, className) {

    override val toJsonType: GenericTypeJson
        get() {
            return GenericTypeJson(
                id = "",
                className = className,
                fieldName = fieldName,
                type = type,
                valueString = "",
                newValueString = "",
                customValues = values.map { it.toJsonType }
            )
        }

}

class MyField(
    val name: String,
    val type: Class<*>,
    var isAccessible: Boolean,
    var get: (instance: Any?) -> Any?,
    var set: (instance: Any?, newValue: Any?) -> Unit,
    var genericType: Type? = null,
    val field: Field? = null,
)

fun convertToMyField(field: Field): MyField {
    return MyField(
        name = field.name,
        type = field.type,
        isAccessible = field.isAccessible,
        get = field::get,
        set = field::set,
        genericType = field.genericType,
        field = field
    )
}

fun processValue(
    className: String,
    type: BaseTypes,
    reference: MyField,
    parentReference: GenericManagedVariable?
): GenericVariable {
    val currentManager = if (parentReference == null) SimpleVariable(
        reference,
        className
    ) else NestedVariable(
        reference,
        parentReference,
        className
    )
    if (type in listOf(
            BaseTypes.INT, BaseTypes.DOUBLE, BaseTypes.FLOAT,
            BaseTypes.LONG, BaseTypes.STRING, BaseTypes.BOOLEAN, BaseTypes.ENUM
        )
    ) {
        return currentManager
    }

    if (type == BaseTypes.CUSTOM) {
        reference.isAccessible = true
        val customValues = reference.type.declaredFields.mapNotNull { field ->
            field.isAccessible = true
            if (field.isAnnotationPresent(IgnoreConfigurable::class.java)) return@mapNotNull null

            val customNewField = convertToMyField(field)

            processValue(
                className,
                getType(field.type, customNewField, reference),
                customNewField,
                currentManager
            )
        }

        return CustomVariable(reference.name, className, customValues, BaseTypes.CUSTOM)
    }

    if (type == BaseTypes.ARRAY) {
        val componentType = reference.type.componentType
        val componentValues = currentManager.manager.getValue()
        if (componentValues != null && componentType != null) {
            val arrayValues = (0 until Array.getLength(componentValues)).map { i ->

                val value = Array.get(componentValues, i)

                val arrayType = getType(componentType)

                val itemType = if (arrayType == BaseTypes.UNKNOWN) {
                    getType(value.javaClass)
                } else {
                    arrayType
                }
                val cType = if (arrayType == BaseTypes.UNKNOWN) {
                    value.javaClass
                } else {
                    componentType
                }

                println("DASH: Array type: $itemType")

                val currentElementReference = MyField(
                    name = i.toString(),
                    type = cType,
                    isAccessible = true,
                    get = { instance ->
                        Array.get(instance, i)
                    },
                    set = { instance, newValue ->
                        Array.set(instance, i, newValue)
                    },
                    genericType = componentType
                )

                processValue(
                    className,
                    itemType,
                    currentElementReference,
                    currentManager
                )
            }
            return CustomVariable(reference.name, className, arrayValues, BaseTypes.ARRAY)
        } else {
            return UnknownVariable(className, reference.name)
        }
    }

    if (type == BaseTypes.LIST) {
        val listInstance = currentManager.manager.getValue() as? MutableList<Any?>
        if (listInstance != null) {

            val listValues = listInstance.mapIndexed { index, value ->
                val arrayType = getType(reference.type.componentType)

                val itemType = if (arrayType == BaseTypes.UNKNOWN) {
                    getType(value?.javaClass ?: Any::class.java)
                } else {
                    arrayType
                }
                val cType = if (arrayType == BaseTypes.UNKNOWN) {
                    value?.javaClass ?: Any::class.java
                } else {
                    reference.type.componentType
                }

                println("DASH: Array type: $itemType")

                val currentElementReference = MyField(
                    name = index.toString(),
                    type = cType,
                    isAccessible = true,
                    get = { instance ->
                        listInstance.getOrNull(index)
                    },
                    set = { instance, newValue ->
                        if (index in listInstance.indices) {
                            listInstance[index] = newValue
                        } else {
                            listInstance.add(newValue)
                        }
                    },
                    genericType = reference.type.componentType
                )

                processValue(
                    className,
                    itemType,
                    currentElementReference,
                    currentManager
                )
            }

            return CustomVariable(reference.name, className, listValues, BaseTypes.LIST)
        }
    }

    if (type == BaseTypes.MAP) {
        val mapInstance = currentManager.manager.getValue() as? MutableMap<*, *>
        if (mapInstance != null) {
            println("   DASH: Map keys: ${mapInstance.map { (key, value) -> "$key.$value" }}")
            val mapValues: List<GenericVariable> = mapInstance.map { (key, _) ->
                println("   DASH: Map key: $key")
                val itemType = getType(mapInstance[key]?.javaClass)
                val currentElementReference = MyField(
                    name = key.toString(),
                    type = mapInstance[key]?.javaClass ?: Any::class.java,
                    isAccessible = true,
                    get = { instance ->
                        mapInstance[key]
                    },
                    set = { instance, newValue ->
                        (mapInstance as MutableMap<Any, Any>)[key as Any] = newValue as Any
                    },
                    genericType = reference.type.componentType
                )

                processValue(
                    className,
                    itemType,
                    currentElementReference,
                    currentManager
                )
            }

            println("   DASH: Map keys2: ${mapValues.map { it.toJsonType }}")


            return CustomVariable(reference.name, className, mapValues, BaseTypes.MAP)
        }
    }

    return UnknownVariable(className, reference.name)
}

class GenericField(
    var className: String,
    var reference: Field,
) {

    val type = getType(reference.type, convertToMyField(reference), null)
    val value: GenericVariable = processValue(className, type, convertToMyField(reference), null)

    val name: String
        get() = reference.name

    fun debug() {
        println("   DASH: Of type $type")
    }

    val toJsonType: GenericTypeJson
        get() = value.toJsonType
}
