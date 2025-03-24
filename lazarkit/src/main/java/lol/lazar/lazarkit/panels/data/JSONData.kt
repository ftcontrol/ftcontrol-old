package lol.lazar.lazarkit.panels.data

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta.Flavor

@Serializable
@Polymorphic
sealed class JSONData

@Serializable
@SerialName("test")
data class TestObject(
    val data: String
) : JSONData()

@Serializable
@SerialName("time")
data class TimeObject(
    val time: String
) : JSONData()

@Serializable
@SerialName("opModeInfo")
data class OpModeInfo(
    var name: String,
    var group: String,
    var flavour: Flavor
) : JSONData()

@Serializable
@SerialName("activeOpMode")
data class ActiveOpMode(
    var opMode: OpModeInfo,
    var status: String
) : JSONData()

@Serializable
@SerialName("getOpmodes")
data object GetOpModesRequest : JSONData()

@Serializable
@SerialName("opmodes")
data class ReceivedOpModes(
    var opModes: List<OpModeInfo>
) : JSONData()

@Serializable
@SerialName("getActiveOpMode")
data object GetActiveOpModeRequest : JSONData()

@Serializable
@SerialName("initOpMode")
data class InitOpModeRequest(
    var opModeName: String
) : JSONData()

@Serializable
@SerialName("startActiveOpMode")
data object StartActiveOpModeRequest : JSONData()

@Serializable
@SerialName("stopActiveOpMode")
data object StopActiveOpModeRequest : JSONData()

@Serializable
@SerialName("telemetryPacket")
data class TelemetryPacket(
    var lines: List<String>
) : JSONData()

val json = Json {
    serializersModule = SerializersModule {
        polymorphic(JSONData::class) {
            subclass(TestObject::class)
            subclass(TimeObject::class)
            subclass(OpModeInfo::class)
            subclass(ActiveOpMode::class)
            subclass(GetOpModesRequest::class)
            subclass(ReceivedOpModes::class)
            subclass(GetActiveOpModeRequest::class)
            subclass(InitOpModeRequest::class)
            subclass(StartActiveOpModeRequest::class)
            subclass(StopActiveOpModeRequest::class)
            subclass(TelemetryPacket::class)
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
    return json.decodeFromString(
        ListSerializer(PolymorphicSerializer(JSONData::class)),
        this
    ) as List<T>
}
