package com.bylazar.ftcontrol.panels.configurables.variables

import com.bylazar.ftcontrol.panels.configurables.annotations.IgnoreConfigurable
import com.bylazar.ftcontrol.panels.configurables.variables.generics.GenericVariable
import com.bylazar.ftcontrol.panels.configurables.variables.instances.CustomVariable
import com.bylazar.ftcontrol.panels.configurables.variables.instances.RecursionReachedVariable
import com.bylazar.ftcontrol.panels.configurables.variables.instances.UnknownVariable
import com.bylazar.ftcontrol.panels.configurables.variables.instances.UnsupportedVariable
import com.qualcomm.robotcore.hardware.HardwareMap
import java.lang.reflect.Array

const val MAX_RECURSION_DEPTH = 32

class RecursionDetectedException(val fieldName: String) : RuntimeException()

fun processValue(
    recursionDepth: Int,
    className: String,
    type: BaseTypes,
    reference: MyField,
    parentReference: MyField?,
    recursionItems: MutableList<String> = mutableListOf(),
): GenericVariable {
    try {
        println("PANELS: Unsupported package ${reference.genericType?.javaClass?.packageName}")

        if (reference.type == HardwareMap::class.java) {
            return UnsupportedVariable(className, reference.name)
        }

        if (recursionItems.contains("this\$0") && reference.name == "this\$0") {
            println("PANELS: Detected self-reference or cycle: $recursionItems")
            throw RecursionDetectedException(reference.name)
        }

        recursionItems.add(reference.name)
        if (recursionDepth > MAX_RECURSION_DEPTH) {
            println("PANELS: Recursion depth exceeded: $recursionItems")
            return UnknownVariable(className, reference.name)
            throw RecursionDetectedException(reference.name)
        }
        val currentManager = convertToMyField(reference, parentReference, className)
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
                    recursionDepth + 1,
                    className,
                    getType(field.type, customNewField, reference),
                    customNewField,
                    currentManager,
                    recursionItems.toMutableList(),
                )
            }

            return CustomVariable(reference.name, className, customValues, BaseTypes.CUSTOM)
        }

        if (type == BaseTypes.ARRAY) {
            val componentType = reference.type.componentType
            val componentValues = currentManager.getValue()
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

                    println("PANELS: Array type: $itemType")

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
                        recursionDepth + 1,
                        className,
                        itemType,
                        currentElementReference,
                        currentManager,
                        recursionItems.toMutableList(),
                    )
                }
                return CustomVariable(reference.name, className, arrayValues, BaseTypes.ARRAY)
            } else {
                return UnknownVariable(className, reference.name)
            }
        }

        if (type == BaseTypes.LIST) {
            val listInstance = currentManager.getValue() as? MutableList<Any?>
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

                    println("PANELS: Array type: $itemType")

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
                        recursionDepth + 1,
                        className,
                        itemType,
                        currentElementReference,
                        currentManager,
                        recursionItems.toMutableList(),
                    )
                }

                return CustomVariable(reference.name, className, listValues, BaseTypes.LIST)
            }
        }

        if (type == BaseTypes.MAP) {
            val mapInstance = currentManager.getValue() as? MutableMap<*, *>
            if (mapInstance != null) {
                println("   PANELS: Map keys: ${mapInstance.map { (key, value) -> "$key.$value" }}")
                val mapValues: List<GenericVariable> = mapInstance.map { (key, _) ->
                    println("   PANELS: Map key: $key")
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
                        recursionDepth + 1,
                        className,
                        itemType,
                        currentElementReference,
                        currentManager,
                        recursionItems.toMutableList(),
                    )
                }

                println("   PANELS: Map keys2: ${mapValues.map { it.toJsonType }}")


                return CustomVariable(reference.name, className, mapValues, BaseTypes.MAP)
            }
        }

        return UnknownVariable(className, reference.name)
    } catch (e: RecursionDetectedException) {
        if (recursionDepth == 0) {
            println("PANELS: Recursion detected and aborted at top-level.")
            return RecursionReachedVariable(className, reference.name)
        } else {
            throw e
        }
    }
}