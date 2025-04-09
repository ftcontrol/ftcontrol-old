package lol.lazar.lazarkit.panels.oldConfs

import lol.lazar.lazarkit.panels.json.GenericTypeJson
import java.lang.reflect.Array

class ArrayElement(
    var array: GenericField,
    var index: Int
) : BaseGenericField(
    id = array.id + "-" + index,
    reference = array.reference,
    className = array.className,
) {

    var customValues: List<GenericField>? = null
    var arrayValues: List<ArrayElement>? = null
    var mapValues: List<MapValueElement>? = null
    var listValues: List<ListElement>? = null

    init {
        Configurables.fieldsMap[id] = this

        if (type == Types.CUSTOM) {

        }

        if (type == Types.ARRAY) {

        }

        if (type == Types.LIST) {

        }

        if (type == Types.MAP) {

        }
    }

    override val type: Types
        get() {
            val arrayType = getType(array.reference.type.componentType)

            if (arrayType == Types.UNKNOWN) {
                val value = currentValue ?: return Types.UNKNOWN
                return getType(value.javaClass)
            }

            return arrayType
        }

    override var currentValue: Any?
        get() {
            val componentType = array.reference.type.componentType
            val componentValues = array.currentValue
            if (componentValues != null && componentType != null) {
                return Array.get(componentValues, index)
            }
            return null
        }
        set(value) {
            val componentType = array.reference.type.componentType
            val componentValues = array.currentValue
            if (componentValues != null && componentType != null) {
                Array.set(componentValues, index, value)
            }
        }
    override val toJsonType: GenericTypeJson
        get() = GenericTypeJson(
            id = id,
            className = array.className,
            fieldName = index.toString(),
            type = type,
            valueString = currentValue.toString(),
            newValueString = currentValue.toString(),
        )
}