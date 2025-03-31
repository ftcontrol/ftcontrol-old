package lol.lazar.lazarkit.panels.configurables

import lol.lazar.lazarkit.panels.configurables.annotations.IgnoreConfigurable
import lol.lazar.lazarkit.panels.data.GenericTypeJson
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.util.UUID

class GenericField(
    override var className: String,
    override var reference: Field,
    override var parentReference: GenericField? = null,
    override var id: String = UUID.randomUUID().toString()
) : BaseGenericField(
    className = className,
    reference = reference,
    parentReference = parentReference,
    id = id
) {
//    TODO: decorator for quick values to select from

    var customValues: List<GenericField>? = null
    var arrayValues: List<ArrayElement>? = null

    init {
        Configurables.fieldsMap[id] = this

        if (type == Types.CUSTOM) {
            customValues = reference.type.declaredFields.mapNotNull { field ->
                field.isAccessible = true
                val isIgnored = field.isAnnotationPresent(IgnoreConfigurable::class.java)
                if (isIgnored) {
                    return@mapNotNull null
                }

                GenericField(
                    className = className,
                    parentReference = this,
                    reference = field
                )
            }
        }

        if (type == Types.ARRAY) {
            val componentType = reference.type.componentType
            val componentValues = currentValue
            if (componentValues != null && componentType != null) {
                arrayValues = (0 until Array.getLength(componentValues)).map { i ->
                    ArrayElement(this, i)
                }
            }
        }
    }

    override val type: Types
        get() = getType(reference.type)

    override var currentValue: Any?
        get() {
            reference.isAccessible = true
            return try {
                if (parentReference == null) {
                    reference.get(null)
                } else {
                    reference.get(parentReference?.currentValue)
                }
            } catch (e: Exception) {
                println("DASH: Could not get value for ${reference.name}: ${e.message}")
                null
            }
        }
        set(value) {
            reference.isAccessible = true

            try {
                if (parentReference == null) {
                    reference.set(null, value)
                } else {
                    reference.set(parentReference?.currentValue, value)
                    println("DASH: Set ${reference.name} to $value")
                }
            } catch (e: Exception) {
                println("DASH: Could not set value for ${reference.name}: ${e.message}")
            }
        }

    val name: String
        get() = reference.name

    val possibleValues: List<String>? by lazy {
        if (type == Types.ENUM) {
            reference.type.enumConstants.map { it.toString() }
        } else {
            null
        }
    }

    override val toJsonType: GenericTypeJson
        get() = GenericTypeJson(
            id = id,
            className = className,
            fieldName = name,
            type = type,
            valueString = currentValue.toString(),
            newValueString = currentValue.toString(),
            possibleValues = possibleValues,
            customValues = customValues?.map { it.toJsonType },
            arrayValues = arrayValues?.map { it.toJsonType }
        )
}