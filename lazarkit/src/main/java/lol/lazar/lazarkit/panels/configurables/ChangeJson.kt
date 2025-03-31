package lol.lazar.lazarkit.panels.configurables

import kotlinx.serialization.Serializable

@Serializable
class ChangeJson(
    val id: String,
    val newValueString: String
)