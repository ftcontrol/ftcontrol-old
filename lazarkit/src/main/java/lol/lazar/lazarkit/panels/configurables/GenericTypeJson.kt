package lol.lazar.lazarkit.panels.configurables

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("jvmField")
class GenericTypeJson(
    val className: String? = null,
    val fieldName: String,
    val type: GenericType.Types,
    val valueString: String,
    val possibleValues: List<String>? = null,
    val customValues: List<GenericTypeJson>? = null,
    val arrayValues: List<GenericTypeJson>? = null
)