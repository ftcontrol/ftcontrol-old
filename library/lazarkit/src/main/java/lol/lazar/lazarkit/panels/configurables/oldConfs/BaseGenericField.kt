package lol.lazar.lazarkit.panels.configurables.oldConfs
//
//import lol.lazar.lazarkit.panels.oldConfs.annotations.GenericValue
//import lol.lazar.lazarkit.panels.json.GenericTypeJson
//import java.lang.reflect.Field
//import java.lang.reflect.ParameterizedType
//import java.util.UUID
//
//abstract class BaseGenericField(
//    open var className: String,
//    open var reference: Field,
//    open var parentReference: GenericField? = null,
//    open var id: String = UUID.randomUUID().toString()
//) {
//    enum class Types {
//        INT,
//        DOUBLE,
//        STRING,
//        BOOLEAN,
//        FLOAT,
//        LONG,
//        ENUM,
//        ARRAY,
//        MAP,
//        LIST,
//        UNKNOWN,
//        CUSTOM,
//        GENERIC,
//        GENERIC_NO_ANNOTATION
//    }
//
//    abstract var currentValue: Any?
//
//    abstract val toJsonType: GenericTypeJson
//
//
//
//    abstract val type: Types
//}