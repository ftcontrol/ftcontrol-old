package com.bylazar.ftcontrol.panels.configurables

import android.content.Context
import com.bylazar.ftcontrol.panels.Logger

object ConfigurablesManager {

    private var classFinder = ClassFinder()

    fun init(context: Context){
        Logger.configurablesLog("Initializing configurables")

        Logger.configurablesLog("Stage 1: Searching for configurables classes...")

        classFinder.updateClasses(context.packageCodePath)

        Logger.configurablesLog("Found ${classFinder.classes.size} configurable classes:")
        classFinder.classes.forEach { className ->
            Logger.configurablesLog("$className")
        }
    }
}