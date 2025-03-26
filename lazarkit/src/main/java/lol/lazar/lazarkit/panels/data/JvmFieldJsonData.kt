package lol.lazar.lazarkit.panels.data

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lol.lazar.lazarkit.panels.GlobalData

@Serializable
@Polymorphic
sealed class JvmFieldInfoBase

@Serializable
@SerialName("string")
data class JvmFieldInfoBoolean(
    val className: String,
    val fieldName: String,
    val currentValue: Boolean
) : JvmFieldInfoBase()

fun setField(item: JvmFieldInfoBoolean) {
    val field =
        GlobalData.jvmFields.find { it.className == item.className && it.field.name == item.fieldName }
    if (field == null) return
    field.field.set(null, item.currentValue)
}

@Serializable
@SerialName("string")
data class JvmFieldInfoFloat(
    val className: String,
    val fieldName: String,
    val currentValue: Float
) : JvmFieldInfoBase()

fun setField(item: JvmFieldInfoFloat) {
    val field =
        GlobalData.jvmFields.find { it.className == item.className && it.field.name == item.fieldName }
    if (field == null) return
    field.field.set(null, item.currentValue)
}

@Serializable
@SerialName("string")
data class JvmFieldInfoLong(
    val className: String,
    val fieldName: String,
    val currentValue: Long
) : JvmFieldInfoBase()

fun setField(item: JvmFieldInfoLong) {
    val field =
        GlobalData.jvmFields.find { it.className == item.className && it.field.name == item.fieldName }
    if (field == null) return
    field.field.set(null, item.currentValue)
}

@Serializable
@SerialName("string")
data class JvmFieldInfoString(
    val className: String,
    val fieldName: String,
    val currentValue: String
) : JvmFieldInfoBase()

fun setField(item: JvmFieldInfoString) {
    val field =
        GlobalData.jvmFields.find { it.className == item.className && it.field.name == item.fieldName }
    if (field == null) return
    field.field.set(null, item.currentValue)
}

@Serializable
@SerialName("unknown")
data class JvmFieldInfoUnknown(
    val className: String,
    val fieldName: String,
) : JvmFieldInfoBase()

fun setField(item: JvmFieldInfoUnknown) {
}

@Serializable
@SerialName("int")
data class JvmFieldInfoInt(
    val className: String,
    val fieldName: String,
    val currentValue: Int
) : JvmFieldInfoBase()

fun setField(item: JvmFieldInfoInt) {
    val field =
        GlobalData.jvmFields.find { it.className == item.className && it.field.name == item.fieldName }
    if (field == null) return
    field.field.set(null, item.currentValue)
}

@Serializable
@SerialName("double")
data class JvmFieldInfoDouble(
    val className: String,
    val fieldName: String,
    val currentValue: Double
) : JvmFieldInfoBase()

fun setField(item: JvmFieldInfoDouble) {
    val field =
        GlobalData.jvmFields.find { it.className == item.className && it.field.name == item.fieldName }
    if (field == null) return
    field.field.set(null, item.currentValue)
}

@Serializable
@SerialName("array")
data class JvmFieldInfoArray(
    val className: String,
    val fieldName: String,
    val values: List<JvmFieldInfoBase>
) : JvmFieldInfoBase()

fun setField(item: JvmFieldInfoArray) {
    val field =
        GlobalData.jvmFields.find { it.className == item.className && it.field.name == item.fieldName }
    if (field == null) return

    when (field.field.type.componentType) {
        Int::class.java -> {
            field.field.set(
                null,
                item.values.filterIsInstance<JvmFieldInfoInt>()
                    .map { it.currentValue }.toIntArray()
            )
        }

        Double::class.java -> {
            field.field.set(
                null,
                item.values.filterIsInstance<JvmFieldInfoDouble>()
                    .map { it.currentValue }.toDoubleArray()
            )
        }

        String::class.java -> {
            field.field.set(
                null,
                item.values.filterIsInstance<JvmFieldInfoString>()
                    .map { it.currentValue }.toTypedArray()
            )
        }

        Boolean::class.java -> {
            field.field.set(
                null,
                item.values.filterIsInstance<JvmFieldInfoBoolean>()
                    .map { it.currentValue }.toBooleanArray()
            )
        }

        Float::class.java -> {
            field.field.set(
                null,
                item.values.filterIsInstance<JvmFieldInfoFloat>()
                    .map { it.currentValue }.toFloatArray()
            )
        }

        Long::class.java -> {
            field.field.set(
                null,
                item.values.filterIsInstance<JvmFieldInfoLong>()
                    .map { it.currentValue }.toLongArray()
            )
        }
    }
}


@Serializable
@SerialName("getJvmFieldsRequest")
data object GetJvmFieldsRequest : JSONData()

@Serializable
@SerialName("jvmFields")
data class ReceivedJvmFields(
    var fields: List<JvmFieldInfoBase>
) : JSONData()