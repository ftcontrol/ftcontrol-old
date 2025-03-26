package lol.lazar.lazarkit.panels.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class JvmFieldInfoBase

@Serializable
@SerialName("string")
data class JvmFieldInfoString(
    val className: String,
    val fieldName: String,
    val currentValue: String
) : JvmFieldInfoBase()

@Serializable
@SerialName("unknown")
data class JvmFieldInfoUnknown(
    val className: String,
    val fieldName: String,
) : JvmFieldInfoBase()

@Serializable
@SerialName("int")
data class JvmFieldInfoInt(
    val className: String,
    val fieldName: String,
    val currentValue: Int
) : JvmFieldInfoBase()

@Serializable
@SerialName("double")
data class JvmFieldInfoDouble(
    val className: String,
    val fieldName: String,
    val currentValue: Double
) : JvmFieldInfoBase()

@Serializable
@SerialName("array")
data class JvmFieldInfoArray(
    val className: String,
    val fieldName: String,
    val values: List<JvmFieldInfoBase>
) : JvmFieldInfoBase()

@Serializable
@SerialName("getJvmFieldsRequest")
data object GetJvmFieldsRequest : JSONData()

@Serializable
@SerialName("jvmFields")
data class ReceivedJvmFields(
    var fields: List<JvmFieldInfoBase>
) : JSONData()