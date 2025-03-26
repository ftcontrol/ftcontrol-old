package lol.lazar.lazarkit.panels.data

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

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
            subclass(GetJvmFieldsRequest::class)
            subclass(ReceivedJvmFields::class)
        }

        polymorphic(JvmFieldInfoBase::class) {
            subclass(JvmFieldInfoString::class)
            subclass(JvmFieldInfoInt::class)
            subclass(JvmFieldInfoDouble::class)
            subclass(JvmFieldInfoArray::class)
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