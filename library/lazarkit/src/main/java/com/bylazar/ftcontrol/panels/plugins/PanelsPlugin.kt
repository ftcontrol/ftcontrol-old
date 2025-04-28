package com.bylazar.ftcontrol.panels.plugins

interface PanelsPlugin {
    val name: String
    fun onRegister()

    fun test(){
        val x = 0
    }
}