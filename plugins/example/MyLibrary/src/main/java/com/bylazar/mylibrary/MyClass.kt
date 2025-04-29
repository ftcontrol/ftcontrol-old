package com.bylazar.mylibrary

import com.bylazar.ftcontrol.panels.plugins.ModContext
import com.bylazar.ftcontrol.panels.plugins.PanelsPlugin

class MyClass : PanelsPlugin() {
    override var id: String = "com.bylazar.myplugin"
    override val name: String = "Lazar's Example Plugin"
    override fun onRegister(context: ModContext) {
        println("DASH: ran internal plugin register")
        createPage("Test Page 1")

        createPage("Test Page 2")
    }
}