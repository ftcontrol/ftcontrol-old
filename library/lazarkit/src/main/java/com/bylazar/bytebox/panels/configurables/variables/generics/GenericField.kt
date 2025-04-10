package com.bylazar.bytebox.panels.configurables.variables.generics

import com.bylazar.bytebox.panels.configurables.variables.convertToMyField
import com.bylazar.bytebox.panels.configurables.variables.getType
import com.bylazar.bytebox.panels.configurables.variables.processValue
import com.bylazar.bytebox.panels.json.GenericTypeJson
import java.lang.reflect.Field

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