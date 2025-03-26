package lol.lazar.lazarkit.panels.data

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
@SerialName("telemetryPacket")
data class TelemetryPacket(
    var lines: List<String>
) : JSONData()