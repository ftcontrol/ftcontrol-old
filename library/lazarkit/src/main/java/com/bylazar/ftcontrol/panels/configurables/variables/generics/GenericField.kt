package com.bylazar.ftcontrol.panels.configurables.variables.generics

import com.bylazar.ftcontrol.panels.Logger
import com.bylazar.ftcontrol.panels.configurables.variables.BaseTypes
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

    val isNull: Boolean
        get() = toJsonType.type == BaseTypes.JSON_ERROR

    val name: String
        get() = reference.name

    fun debug() {
        Logger.configurablesLog("Debug for $className / ${reference.name}")
        Logger.configurablesLog("Of type $type")
    }

    val toJsonType: GenericTypeJson
        get() {
            try {
                val json = value.toJsonType
                return json
            } catch (t: Throwable) {
                println("PANELS: CONFIGURABLES: JSON Error for $className / ${reference.name}: ${t.message}")
                return JSONErrorVariable(className, reference.name).toJsonType
            }
        }
}