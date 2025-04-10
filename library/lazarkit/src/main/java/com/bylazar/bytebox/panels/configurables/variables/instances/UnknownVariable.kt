package com.bylazar.bytebox.panels.configurables.variables.instances

import com.bylazar.bytebox.panels.configurables.variables.BaseTypes
import com.bylazar.bytebox.panels.configurables.variables.generics.GenericVariable
import com.bylazar.bytebox.panels.json.GenericTypeJson

class UnknownVariable(
    override val className: String,
    val name: String
) : GenericVariable(className) {
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