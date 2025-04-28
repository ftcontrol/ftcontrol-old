package com.bylazar.ftcontrol.panels.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Plugin(
    val id: String,
    val name: String
)

@Serializable
@SerialName("plugins")
data class ReceivedPlugins(
    val plugins: List<Plugin>
) : JSONData()