package lol.lazar.lazarkit.panels.configurables.variables

import lol.lazar.lazarkit.panels.json.GenericTypeJson
import lol.lazar.lazarkit.panels.oldConfs.annotations.IgnoreConfigurable
import java.lang.reflect.Field

class CustomVariable(
    var className: String,
    var reference: Field,
) : GenericVariable(
) {

    inner class CustomManager: GenericManager(){
        override fun getValue(): Any? {

        }

        override fun setValue(value: String): Boolean {
            TODO("Not yet implemented")
        }

    }

    var manager = CustomManager()

    var customValues: List<NestedSimpleVariable> =
        reference.type.declaredFields.mapNotNull { field ->
            field.isAccessible = true
            val isIgnored = field.isAnnotationPresent(IgnoreConfigurable::class.java)
            if (isIgnored) {
                return@mapNotNull null
            }

            NestedSimpleVariable(
                className = className,
                parentReference = manager,
                reference = field
            )
        }

    override val toJsonType: GenericTypeJson
        get() = GenericTypeJson(
            className = className,
            fieldName = reference.name,
            type = BaseTypes.CUSTOM,
            possibleValues = null,
            customValues = customValues.map { it.toJsonType },
            isOpened = false,
            id = "none",
            valueString = "",
            newValueString = "",
        )
}