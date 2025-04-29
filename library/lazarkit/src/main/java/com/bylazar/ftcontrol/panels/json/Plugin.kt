package com.bylazar.ftcontrol.panels.json

import com.bylazar.ftcontrol.panels.plugins.Page
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Plugin(
    val id: String,
    val name: String,
    val pages: List<Page>
)

@Serializable
@SerialName("plugins")
data class ReceivedPlugins(
    val plugins: List<Plugin>
) : JSONData()