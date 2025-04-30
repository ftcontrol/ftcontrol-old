package com.bylazar.ftcontrol.panels.plugins

import android.content.Context
import com.bylazar.ftcontrol.panels.CorePanels

object PluginManager {
    val plugins = mutableMapOf<String, PanelsPlugin>()
    private val finder = ClassFinder()

    fun onRegister(corePanels: CorePanels) {
        val modContext = ModContext()
        plugins.forEach { (id, plugin) ->
            try {
                plugin.onRegister(modContext)
            } catch (e: Exception) {
                println("DASH: Failed to register plugin $id")
                e.printStackTrace()
            }
        }
    }

    fun loadPlugins(context: Context) {
        println("DASH: Loading plugins...")
        plugins.clear()
        finder.apkPath = context.packageCodePath

        val foundClasses = finder.getAllClasses

        println("DASH: Found ${foundClasses.size} classes:")

        for (classEntry in foundClasses) {
            try {
                val clazz = Class.forName(classEntry.className)

                if (PanelsPlugin::class.java.isAssignableFrom(clazz)) {
                    println("DASH: Found plugin implementation: ${clazz.name}")

                    val constructor = clazz.getDeclaredConstructor()
                    val pluginInstance = constructor.newInstance() as PanelsPlugin

                    val originalId = pluginInstance.id
                    var uniqueId = originalId
                    var suffix = 1

                    while (plugins.containsKey(uniqueId)) {
                        println("DASH: Plugin ID '$originalId' is already registered. Renaming...")
                        uniqueId = "$originalId${suffix++}"
                    }

                    pluginInstance.id = uniqueId
                    plugins[uniqueId] = pluginInstance

                    println("DASH: Successfully registered plugin: ${clazz.name} with ID '$uniqueId'")
                }
            } catch (e: ClassNotFoundException) {
                println("DASH: Class not found: ${classEntry.className}")
                e.printStackTrace()
            } catch (e: ClassCastException) {
                println("DASH: Class does not implement PanelsPlugin: ${classEntry.className}")
                e.printStackTrace()
            } catch (e: Exception) {
                println("DASH: Unexpected error loading plugin class: ${classEntry.className}")
                e.printStackTrace()
            }
        }
    }
}