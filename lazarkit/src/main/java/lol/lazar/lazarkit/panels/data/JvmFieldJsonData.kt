package lol.lazar.lazarkit.panels.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.lazar.lazarkit.panels.configurables.ChangeJson
import lol.lazar.lazarkit.panels.configurables.GenericTypeJson


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