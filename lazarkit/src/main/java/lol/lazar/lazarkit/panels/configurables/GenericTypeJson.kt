package lol.lazar.lazarkit.panels.configurables

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("jvmField")
class GenericTypeJson(
    val id: String,
    val className: String? = null,
    val fieldName: String,
    val type: GenericType.Types,
    val valueString: String,
    val newValueString: String,
    val value: String = "",
    val isValid: Boolean = true,
    val possibleValues: List<String>? = null,
    val customValues: List<GenericTypeJson>? = null,
    val arrayValues: List<GenericTypeJson>? = null,
)