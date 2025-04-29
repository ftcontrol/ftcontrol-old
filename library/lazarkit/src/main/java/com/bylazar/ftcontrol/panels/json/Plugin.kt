package com.bylazar.ftcontrol.panels.json

import com.bylazar.ftcontrol.panels.plugins.PluginJson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("plugins")
data class ReceivedPlugins(
    val plugins: List<PluginJson>
) : JSONData()

@Serializable
@SerialName("pluginsUpdate")
data class PluginsUpdate(
    val plugins: List<PluginJson>
) : JSONData()