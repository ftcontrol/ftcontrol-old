package lol.lazar.lazarkit.panels.configurables.variables.generics

import lol.lazar.lazarkit.panels.configurables.variables.MyField
import lol.lazar.lazarkit.panels.json.GenericTypeJson

abstract class GenericVariable(
    open val reference: MyField?,
    open val className: String,
) {
    abstract val toJsonType: GenericTypeJson
}