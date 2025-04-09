package lol.lazar.lazarkit.panels.configurables.oldConfs
//
//import lol.lazar.lazarkit.panels.json.GenericTypeJson
//
//class MapValueElement(
//    var map: GenericField,
//    val key: Any,
//) : BaseGenericField(
//    id = map.id + "-" + key.toString(),
//    reference = map.reference,
//    className = key.toString(),
//    parentReference = map.parentReference
//) {
//    init {
//        Configurables.fieldsMap[id] = this
//    }
//
//    override val type: Types
//        get() = getType(
//            (map.currentValue as Map<*, *>)[key]?.javaClass
//        )
//
//    override var currentValue: Any?
//        get() = (map.currentValue as Map<*, *>)[key]
//        set(newValue) {
//            val mapInstance = map.currentValue as? MutableMap<Any, Any>
//            if (mapInstance != null && currentValue != null) {
//                mapInstance[key] = newValue as Any
//            }
//        }
//
//    override val toJsonType: GenericTypeJson
//        get() = GenericTypeJson(
//            id = id,
//            className = map.className,
//            fieldName = key.toString(),
//            type = type,
//            valueString = currentValue.toString(),
//            newValueString = currentValue.toString(),
//        )
//}