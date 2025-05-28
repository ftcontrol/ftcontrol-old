package com.bylazar.ftcontrol.panels.configurables

import android.content.Context
import com.bylazar.ftcontrol.panels.Logger
import com.bylazar.ftcontrol.panels.configurables.variables.MyField
import com.bylazar.ftcontrol.panels.configurables.variables.generics.GenericField
import com.bylazar.ftcontrol.panels.json.GenericTypeJson

object ConfigurablesManager {
    var fieldsMap = mutableMapOf<String, MyField>()
    var configurableClasses: List<ClassFinder.ClassEntry> = listOf()

    var jvmFields = listOf<GenericField>()
    var initialJvmFields = listOf<GenericTypeJson>()

    private var classFinder = ClassFinder()
    private var variables = VariablesFinder()

    fun init(context: Context) {
        Logger.configurablesLog("Initializing configurables")

        fieldsMap = mutableMapOf()
        configurableClasses = listOf()
        jvmFields = listOf()

        Logger.configurablesLog("Stage 1: Searching for configurables classes...")

        classFinder.updateClasses(context.packageCodePath)

        configurableClasses = classFinder.classes

        Logger.configurablesLog("Found ${classFinder.classes.size} configurable classes:")
        classFinder.classes.forEach { className ->
            Logger.configurablesLog("$className")
        }

        Logger.configurablesLog("Stage 2: Searching for configurables variables...")

        Logger.configurablesLog("Found ${variables.jvmFields.size} configurable variables:")
        variables.jvmFields.forEach { info ->
            Logger.configurablesLog("${info.className}.${info.reference.name}")
            info.debug()
        }

        Logger.configurablesLog("Stage 3: Converting to JSON...")
        jvmFields = variables.jvmFields
        initialJvmFields = jvmFields.map { it.toJsonType }
    }
}