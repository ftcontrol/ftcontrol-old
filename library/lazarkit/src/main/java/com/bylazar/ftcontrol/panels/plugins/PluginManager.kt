package com.bylazar.ftcontrol.panels.plugins

import android.content.Context
import com.bylazar.ftcontrol.panels.CorePanels
import com.bylazar.ftcontrol.panels.Logger
import dalvik.system.DexClassLoader

object PluginManager {
    val allPlugins = mutableMapOf<String, PanelsPlugin<*>>()
    val plugins: MutableMap<String, PanelsPlugin<*>>
        get() = allPlugins.filter { it.value.isEnabled }.toMutableMap()
    private val finder = ClassFinder()

    fun onRegister(corePanels: CorePanels) {
        val modContext = ModContext()
        plugins.forEach { (id, plugin) ->
            Logger.pluginsLog("Plugin pages count: ${plugin.pages.size}")
            try {
                plugin.onRegister(modContext)
                plugin.cachedPages = plugin.pages.map { it.toJson }
                Logger.pluginsLog("Registered plugin $id")
            } catch (e: Exception) {
                Logger.pluginsError("Failed to register plugin $id, ${e.message}")
                e.printStackTrace()
            }
        }
        Logger.pluginsLog("Registered ${plugins.size} plugins.")
    }

    fun loadPlugins(context: Context) {
        Logger.pluginsLog("Loading plugins...")
        allPlugins.clear()
        finder.apkPath = context.packageCodePath

        val foundClasses = finder.getAllClasses

        Logger.pluginsLog("Found ${foundClasses.size} classes:")

        foundClasses.forEach {
            try {
                val clazz = Class.forName(it.className)

                val pack = clazz.`package`
                if (pack != null && pack.name.startsWith("com.bylazar.ftcontrol")) return@forEach

                if (PanelsPlugin::class.java.isAssignableFrom(clazz)) {
                    Logger.pluginsLog("Found plugin implementation: ${clazz.name}")
                    val constructor = clazz.getDeclaredConstructor()
                    val pluginInstance = constructor.newInstance() as PanelsPlugin<*>

                    val originalId = pluginInstance.id
                    var uniqueId = originalId
                    var suffix = 1

                    while (allPlugins.containsKey(uniqueId)) {
                        Logger.pluginsLog("Plugin ID '$originalId' is already registered. Renaming...")
                        uniqueId = "$originalId${suffix++}"
                    }

                    pluginInstance.id = uniqueId

                    pluginInstance.handleConfig(foundClasses)

                    allPlugins[uniqueId] = pluginInstance

                    Logger.pluginsLog("Successfully registered plugin: ${clazz.name} with ID '$uniqueId'")

                }
            } catch (e: NoClassDefFoundError) {
                Logger.pluginsError("Skipping plugin '${it.className}' due to missing class: ${e.message}")
            } catch (e: ClassNotFoundException) {
                Logger.pluginsError("Class not found: ${it.className}")
            } catch (e: ClassCastException) {
                Logger.pluginsError("Class does not implement PanelsPlugin: ${it.className}")
            } catch (e: Exception) {
                Logger.pluginsError("Unexpected error loading plugin class: ${it.className}")
            } catch(t: Throwable) {
                Logger.pluginsError("Throwable caught: ${t::class.simpleName} - ${t.message}")
            }
        }

        Logger.pluginsLog("Plugins map contents: ${allPlugins.keys}")

        Logger.pluginsLog("Loaded ${allPlugins.size} plugins.")

    }
}