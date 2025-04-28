package com.bylazar.mylibrary

import com.bylazar.ftcontrol.panels.CorePanels
import com.bylazar.ftcontrol.panels.plugins.PanelsPlugin

class MyClass : PanelsPlugin {
    override val id: String = "com.bylazar.myplugin"
    override val name: String = "Lazar's Example Plugin"

    override fun onRegister(corePanels: CorePanels) {
        println("DASH: ran internal plugin register")
    }

}