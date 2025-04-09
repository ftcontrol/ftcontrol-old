package lol.lazar.lazarkit.panels.configurables.variables

import java.lang.reflect.Field
import java.lang.reflect.Type

class MyField(
    val name: String,
    val type: Class<*>,
    var isAccessible: Boolean,
    var get: (instance: Any?) -> Any?,
    var set: (instance: Any?, newValue: Any?) -> Unit,
    var genericType: Type? = null,
    val field: Field? = null,
)

fun convertToMyField(field: Field): MyField {
    return MyField(
        name = field.name,
        type = field.type,
        isAccessible = field.isAccessible,
        get = field::get,
        set = field::set,
        genericType = field.genericType,
        field = field
    )
}