package com.bylazar.mylibrary

import com.bylazar.ftcontrol.panels.plugins.PanelsPlugin

class MyClass : PanelsPlugin {
    override val name: String = "Lazar's Example Plugin"

    override fun onRegister() {
        println("DASH: ran internal plugin register")
    }

}