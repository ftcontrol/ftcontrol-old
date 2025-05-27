package com.bylazar.ftcontrol.panels.configurables.variables.generics

import com.bylazar.ftcontrol.panels.configurables.variables.convertToMyField
import com.bylazar.ftcontrol.panels.configurables.variables.getType
import com.bylazar.ftcontrol.panels.configurables.variables.instances.JSONErrorVariable
import com.bylazar.ftcontrol.panels.configurables.variables.processValue
import com.bylazar.ftcontrol.panels.json.GenericTypeJson
import java.lang.reflect.Field

class GenericField(
    var className: String,
    var reference: Field,
) {

    val type = getType(reference.type, convertToMyField(reference), null)
    val value: GenericVariable = processValue(0, className, type, convertToMyField(reference), null)

    val name: String
        get() = reference.name

    fun debug() {
        println("   PANELS: Of type $type")
    }

    val toJsonType: GenericTypeJson
        get() {
            try {
                val json = value.toJsonType
                return json
            } catch (t: Throwable) {
                return JSONErrorVariable(className, reference.name).toJsonType
            }
        }
}