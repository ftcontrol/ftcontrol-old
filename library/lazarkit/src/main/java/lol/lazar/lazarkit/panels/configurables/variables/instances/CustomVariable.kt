package lol.lazar.lazarkit.panels.configurables.variables.instances

import lol.lazar.lazarkit.panels.configurables.variables.BaseTypes
import lol.lazar.lazarkit.panels.configurables.variables.generics.GenericVariable
import lol.lazar.lazarkit.panels.json.GenericTypeJson

class CustomVariable(
    val fieldName: String,
    override val className: String,
    val values: List<GenericVariable>,
    val type: BaseTypes = BaseTypes.CUSTOM
) : GenericVariable(null, className) {

    override val toJsonType: GenericTypeJson
        get() {
            return GenericTypeJson(
                id = "",
                className = className,
                fieldName = fieldName,
                type = type,
                valueString = "",
                newValueString = "",
                customValues = values.map { it.toJsonType }
            )
        }

}