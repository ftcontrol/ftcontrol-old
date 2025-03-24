package lol.lazar.lazarkit.panels.data

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta.Flavor
import kotlinx.serialization.PolymorphicSerializer

@Serializable
@Polymorphic
sealed class JSONData

@Serializable
@SerialName("test")
data class TestObject(
    val data: String
) : JSONData()

@Serializable
@SerialName("opModeInfo")
data class OpModeInfo(
    var name: String,
    var group: String,
    var flavour: Flavor
) : JSONData()

val json = Json {
    serializersModule = SerializersModule {
        polymorphic(JSONData::class) {
            subclass(TestObject::class)
            subclass(OpModeInfo::class)
        }
    }
    useArrayPolymorphism = false
    classDiscriminator = "kind"
}

inline fun <reified T : JSONData> T.toJson(): String {
    return json.encodeToString(PolymorphicSerializer(JSONData::class), this)
}

inline fun <reified T : JSONData> String.fromJsonToObject(): T {
    return json.decodeFromString(PolymorphicSerializer(JSONData::class), this) as T
}

inline fun <reified T : JSONData> List<T>.toJson(): String {
    return json.encodeToString(ListSerializer(PolymorphicSerializer(JSONData::class)), this)
}

inline fun <reified T : JSONData> String.fromJsonToList(): List<T> {
    return json.decodeFromString(ListSerializer(PolymorphicSerializer(JSONData::class)), this) as List<T>
}
