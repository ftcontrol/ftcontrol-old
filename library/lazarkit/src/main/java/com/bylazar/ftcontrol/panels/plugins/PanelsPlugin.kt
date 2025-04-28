package com.bylazar.ftcontrol.panels.plugins

import com.bylazar.ftcontrol.panels.CorePanels

interface PanelsPlugin {
    var id: String
    val name: String
    fun onRegister(corePanels: CorePanels)
}