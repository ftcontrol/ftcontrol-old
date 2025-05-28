package com.bylazar.ftcontrol.panels

object Logger {
    private val CONFIGURABLES_PREFIX = "Configurables"

    private fun getCallerClassName(): String {
        return Throwable()
            .stackTrace
            .firstOrNull { !it.className.contains("com.bylazar.ftcontrol.panels.Logger") }
            ?.className
            ?.substringAfterLast('.')
            ?: "Unknown"
    }

    fun log(prefix: String, message: String) {
        println("PANELS: ${prefix.uppercase()}: (${getCallerClassName()}): $message")
    }

    fun error(prefix: String, message: String) {
        println("PANELS: ${prefix.uppercase()}: (${getCallerClassName()}): ERROR: $message")
    }

    fun configurablesLog(message: String) = log(CONFIGURABLES_PREFIX, message)
    fun configurablesError(message: String) = error(CONFIGURABLES_PREFIX, message)
}