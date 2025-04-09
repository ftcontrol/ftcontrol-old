package lol.lazar.lazarkit.panels.configurables.variables

import lol.lazar.lazarkit.panels.json.GenericTypeJson
import java.lang.reflect.Field

class GenericField(
    var className: String,
    var reference: Field,
) : GenericVariable() {
    val name: String
        get() = reference.name
    override val toJsonType: GenericTypeJson
        get() = TODO("Not yet implemented")

    val genericType = reference.genericType

    val type = getType(reference.type, reference, null)
}