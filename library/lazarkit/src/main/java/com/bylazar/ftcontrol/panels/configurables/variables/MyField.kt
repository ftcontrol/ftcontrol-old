package com.bylazar.ftcontrol.panels.configurables.variables

import com.bylazar.ftcontrol.panels.configurables.ConfigurablesManager
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
        ConfigurablesManager.fieldsMap[id] = this
    }

    fun getValue(recursionDepth: Int = 0): Any? {
        if(recursionDepth > MAX_RECURSION_DEPTH) return null
        return try {
            get(parentField?.getValue(recursionDepth + 1))
        } catch (e: Exception) {
            println("PANELS: Could not get value for ${name}: ${e.message}")
            throw RuntimeException("PANELS: Could not get value for $name", e)
            null
        }
    }

    fun setValue(newValue: String): Boolean {
        return setValue(convertValue(newValue, myType, ref?.type?.enumConstants) ?: return false)
    }

    fun setValue(newValue: Any): Boolean {
        return try {
            set(parentField?.getValue(), newValue)
            true
        } catch (e: Exception) {
            println("PANELS: Could not set value for ${name}: ${e.message}")
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