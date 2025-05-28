package com.bylazar.ftcontrol.panels.configurablesOld.variables.generics

import com.bylazar.ftcontrol.panels.json.GenericTypeJson

abstract class GenericVariable(
    open val className: String,
) {
    abstract val toJsonType: GenericTypeJson
}