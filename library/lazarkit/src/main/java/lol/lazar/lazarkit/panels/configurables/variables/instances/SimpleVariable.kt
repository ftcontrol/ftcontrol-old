package lol.lazar.lazarkit.panels.configurables.variables.instances

import lol.lazar.lazarkit.panels.configurables.variables.BaseTypes
import lol.lazar.lazarkit.panels.configurables.variables.generics.GenericManagedVariable
import lol.lazar.lazarkit.panels.configurables.variables.generics.GenericManager
import lol.lazar.lazarkit.panels.configurables.variables.MyField
import lol.lazar.lazarkit.panels.configurables.variables.getType
import lol.lazar.lazarkit.panels.json.GenericTypeJson

class SimpleVariable(
    override val reference: MyField,
    override val className: String
) : GenericManagedVariable(reference, className) {
    val type = getType(reference.type, reference, null)

    override val manager = GenericManager(
        type,
        getValue = {
            reference.isAccessible = true
            try {
                reference.get(null)
            } catch (e: Exception) {
                println("DASH: Could not get value for ${reference.name}: ${e.message}")
                null
            }
        },
        setValue = {
            reference.isAccessible = true
            try {
                reference.set(null, it)
                true
            } catch (e: Exception) {
                println("DASH: Could not set value for ${reference.name}: ${e.message}")
                false
            }
        },
        possibleValues = when (type) {
            BaseTypes.BOOLEAN -> listOf("true", "false")
            BaseTypes.ENUM -> reference.type.enumConstants.map { it.toString() }
            else -> null
        }
    )

    override val toJsonType: GenericTypeJson
        get() {
            val value = manager.getValue()?.toString() ?: ""
            return GenericTypeJson(
                id = manager.id,
                className = className,
                fieldName = reference.name,
                type = type,
                valueString = value,
                newValueString = value,
                possibleValues = manager.possibleValues
            )
        }
}