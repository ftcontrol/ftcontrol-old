package com.bylazar.ftcontrol.panels.json

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
@SerialName("telemetryLinesPacket")
data class TelemetryLinesPacket(
    var lines: List<String>,
    var timestamp: Long
) : JSONData()

@Serializable
@SerialName("telemetryGraphPacket")
data class TelemetryGraphPacket(
    var graphs: Map<String, List<GraphPacket>>,
    var timestamp: Long
) : JSONData()

@Serializable
@SerialName("telemetryCanvasPacket")
data class TelemetryCanvasPacket(
    var canvas: Canvas,
    var timestamp: Long
) : JSONData()


@Serializable
data class GraphPacket(
    var data: String,
    var timestamp: Long
)