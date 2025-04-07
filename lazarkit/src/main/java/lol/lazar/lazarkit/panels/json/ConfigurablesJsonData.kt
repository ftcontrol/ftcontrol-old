package lol.lazar.lazarkit.panels.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.lazar.lazarkit.panels.configurables.BaseGenericField


@Serializable
@SerialName("getJvmFieldsRequest")
data object GetJvmFieldsRequest : JSONData()

@Serializable
@SerialName("jvmFields")
data class ReceivedJvmFields(
    var fields: List<GenericTypeJson>
) : JSONData()

@Serializable
@SerialName("updatedJvmFields")
data class UpdatedJvmFields(
    var fields: List<ChangeJson>
) : JSONData()

@Serializable
@SerialName("jvmField")
class GenericTypeJson(
    val id: String,
    val className: String? = null,
    val fieldName: String,
    val type: BaseGenericField.Types,
    val valueString: String,
    val newValueString: String,
    val value: String = "",
    val isValid: Boolean = true,
    val possibleValues: List<String>? = null,
    val customValues: List<GenericTypeJson>? = null,
    val isOpened: Boolean? = null
)

@Serializable
class ChangeJson(
    val id: String,
    val newValueString: String
)