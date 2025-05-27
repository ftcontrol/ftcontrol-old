package com.bylazar.ftcontrol.panels.configurables.variables.instances

import com.bylazar.ftcontrol.panels.configurables.variables.BaseTypes
import com.bylazar.ftcontrol.panels.configurables.variables.generics.GenericVariable
import com.bylazar.ftcontrol.panels.json.GenericTypeJson

class JSONErrorVariable(
    override val className: String,
    val name: String
) : GenericVariable(className) {
    override val toJsonType: GenericTypeJson
        get() = GenericTypeJson(
            id = "",
            className = className,
            fieldName = name,
            type = BaseTypes.JSON_ERROR,
            valueString = "",
            newValueString = "",
        )
}