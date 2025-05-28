package com.bylazar.ftcontrol.panels.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.bylazar.ftcontrol.panels.configurables.variables.BaseTypes


@Serializable
@SerialName("getJvmFieldsRequest")
data object GetJvmFieldsRequest : JSONData()

@Serializable
@SerialName("jvmFields")
data class ReceivedJvmFields(
    var fields: List<GenericTypeJson>
) : JSONData()

@Serializable
@SerialName("initialJvmFields")
data class ReceivedInitialJvmFields(
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
    val type: BaseTypes,
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