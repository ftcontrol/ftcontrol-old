package lol.lazar.lazarkit.panels.oldConfs

import lol.lazar.lazarkit.panels.json.GenericTypeJson

class ListElement(
    var listField: GenericField,
    var index: Int
) : BaseGenericField(
    id = listField.id + "-list-" + index,
    reference = listField.reference,
    className = listField.className,
) {

    init {
        Configurables.fieldsMap[id] = this
    }

    override val type: Types
        get() {
            val list = listField.currentValue as? List<*> ?: return Types.UNKNOWN
            val item = list.getOrNull(index) ?: return Types.UNKNOWN
            return getType(item.javaClass)
        }

    override var currentValue: Any?
        get() {
            val list = listField.currentValue as? List<*>
            return list?.getOrNull(index)
        }
        set(value) {
            val list = listField.currentValue as? MutableList<Any?>
            if (list != null && index in list.indices) {
                list[index] = value
            }
        }

    override val toJsonType: GenericTypeJson
        get() = GenericTypeJson(
            id = id,
            className = listField.className,
            fieldName = index.toString(),
            type = type,
            valueString = currentValue.toString(),
            newValueString = currentValue.toString(),
        )
}
