package com.bylazar.bytebox.panels.configurables.variables.generics

import com.bylazar.bytebox.panels.json.GenericTypeJson

abstract class GenericVariable(
    open val className: String,
) {
    abstract val toJsonType: GenericTypeJson
}