package com.bylazar.ftcontrol.panels.configurablesOld.variables.instances

import com.bylazar.ftcontrol.panels.configurablesOld.variables.BaseTypes
import com.bylazar.ftcontrol.panels.configurablesOld.variables.generics.GenericVariable
import com.bylazar.ftcontrol.panels.json.GenericTypeJson

class RecursionReachedVariable(
    override val className: String,
    val name: String
) : GenericVariable(className) {
    override val toJsonType: GenericTypeJson
        get() = GenericTypeJson(
            id = "",
            className = className,
            fieldName = name,
            type = BaseTypes.RECURSION_REACHED,
            valueString = "",
            newValueString = "",
        )
}