package com.bylazar.ftcontrol.panels.configurables.variables

import com.bylazar.ftcontrol.panels.configurables.Configurables
import com.bylazar.ftcontrol.panels.configurables.variables.generics.GenericVariable
import com.bylazar.ftcontrol.panels.json.GenericTypeJson
import java.lang.reflect.Field
import java.lang.reflect.Type
import java.util.UUID

class MyField(
    val name: String,
    val type: Class<*>,
    var isAccessible: Boolean,
    var get: (instance: Any?) -> Any?,
    var set: (instance: Any?, newValue: Any?) -> Unit,
    var genericType: Type? = null,
    val ref: Field? = null,
    val parentField: MyField? = null,
    override val className: String = ""
) : GenericVariable(className) {
    val myType = getType(type, this, parentField)
    val possibleValues = when (myType) {
        BaseTypes.BOOLEAN -> listOf("true", "false")
        BaseTypes.ENUM -> ref?.type?.enumConstants?.map { it.toString() }
        else -> null
    }
    val id = UUID.randomUUID().toString()

    init {
        if (ref != null) ref.isAccessible = true
        Configurables.fieldsMap[id] = this
    }

    fun getValue(): Any? {
        return try {
            get(parentField?.getValue())
        } catch (e: Exception) {
            println("DASH: Could not get value for ${name}: ${e.message}")
            null
        }
    }

    fun setValue(newValue: String): Boolean {
        return setValue(convertValue(newValue, myType, possibleValues) ?: return false)
    }

    fun setValue(newValue: Any): Boolean {
        return try {
            set(parentField?.getValue(), newValue)
            true
        } catch (e: Exception) {
            println("DASH: Could not set value for ${name}: ${e.message}")
            false
        }
    }

    override val toJsonType: GenericTypeJson
        get() {
            val value = getValue().toString()
            return GenericTypeJson(
                id = id,
                className = className,
                fieldName = name,
                type = myType,
                valueString = value,
                newValueString = value,
                possibleValues = possibleValues
            )
        }
}

fun convertToMyField(field: Field): MyField {
    return MyField(
        name = field.name,
        type = field.type,
        isAccessible = field.isAccessible,
        get = field::get,
        set = field::set,
        genericType = field.genericType,
        ref = field
    )
}

fun convertToMyField(field: MyField, parentField: MyField?, className: String): MyField {
    return MyField(
        name = field.name,
        type = field.type,
        isAccessible = field.isAccessible,
        get = field.get,
        set = field.set,
        genericType = field.genericType,
        ref = field.ref,
        parentField = parentField,
        className = className,
    )
}