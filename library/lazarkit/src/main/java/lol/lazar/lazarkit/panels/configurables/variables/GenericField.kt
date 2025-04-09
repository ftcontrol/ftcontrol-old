package lol.lazar.lazarkit.panels.configurables.variables

import lol.lazar.lazarkit.panels.configurables.annotations.IgnoreConfigurable
import lol.lazar.lazarkit.panels.json.GenericTypeJson
import java.lang.reflect.Field

class UnknownVariable(
    override val className: String,
    val name: String
) : GenericVariable(null, className) {
    override val manager: GenericManager
        get() = TODO("Not yet implemented")
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

class SimpleVariable(
    override val reference: Field,
    override val className: String
) : GenericVariable(reference, className) {
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

class NestedVariable(
    override val reference: Field,
    val parentReference: GenericVariable,
    override val className: String
) : GenericVariable(reference, className) {
    val type = getType(reference.type, reference, parentReference.reference)

    override val manager = GenericManager(
        type,
        getValue = {
            reference.isAccessible = true
            try {
                reference.get(parentReference.manager.getValue())
            } catch (e: Exception) {
                println("DASH: Could not get value for ${reference.name}: ${e.message}")
                null
            }
        },
        setValue = {
            reference.isAccessible = true
            try {
                reference.set(parentReference.manager.getValue(), it)
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

class CustomVariable(
    val fieldName: String,
    override val className: String,
    val values: List<GenericVariable>
) : GenericVariable(null, className) {
    override val manager: GenericManager
        get() = TODO("Not yet implemented")
    override val toJsonType: GenericTypeJson
        get() {
            return GenericTypeJson(
                id = "",
                className = className,
                fieldName = fieldName,
                type = BaseTypes.CUSTOM,
                valueString = "",
                newValueString = "",
                customValues = values.map { it.toJsonType }
            )
        }

}

fun processValue(
    className: String,
    type: BaseTypes,
    reference: Field,
    parentReference: GenericVariable?
): GenericVariable {
    if (type in listOf(
            BaseTypes.INT, BaseTypes.DOUBLE, BaseTypes.FLOAT,
            BaseTypes.LONG, BaseTypes.STRING, BaseTypes.BOOLEAN, BaseTypes.ENUM
        )
    ) {
        if (parentReference == null) {
            return SimpleVariable(
                reference,
                className
            )
        }

        return NestedVariable(
            reference,
            parentReference,
            className
        )
    }

    if (type == BaseTypes.CUSTOM) {
        reference.isAccessible = true
        val customValues = reference.type.declaredFields.mapNotNull { field ->
            field.isAccessible = true
            if (field.isAnnotationPresent(IgnoreConfigurable::class.java)) return@mapNotNull null

            processValue(
                className,
                getType(field.type, field, reference),
                field,
                if(parentReference == null) SimpleVariable(reference, className) else NestedVariable(
                    reference,
                    parentReference,
                    className
                )
            )
        }

        return CustomVariable(reference.name, className, customValues)
    }

    return UnknownVariable(className, reference.name)
}

class GenericField(
    var className: String,
    var reference: Field,
) {

    val type = getType(reference.type, reference, null)
    val value: GenericVariable = processValue(className, type, reference, null)

    val name: String
        get() = reference.name

    fun debug() {
        println("   DASH: Of type $type")
    }

    val toJsonType: GenericTypeJson
        get() = value.toJsonType
}
