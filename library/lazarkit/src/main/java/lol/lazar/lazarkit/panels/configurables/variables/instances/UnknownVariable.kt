package lol.lazar.lazarkit.panels.configurables.variables.instances

import lol.lazar.lazarkit.panels.configurables.variables.BaseTypes
import lol.lazar.lazarkit.panels.configurables.variables.generics.GenericVariable
import lol.lazar.lazarkit.panels.json.GenericTypeJson

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