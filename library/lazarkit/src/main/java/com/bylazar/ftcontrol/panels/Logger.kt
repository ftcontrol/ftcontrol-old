package com.bylazar.ftcontrol.panels

object Logger {
    private val CONFIGURABLES_PREFIX = "Configurables"

    fun log(prefix: String, message: String) {
        println("PANELS: ${prefix.uppercase()}: $message")
    }

    fun configurablesLog(message: String) = log(CONFIGURABLES_PREFIX, message)
}