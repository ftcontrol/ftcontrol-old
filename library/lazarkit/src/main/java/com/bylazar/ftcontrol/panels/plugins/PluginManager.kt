package com.bylazar.ftcontrol.panels.plugins

import android.content.Context
import com.bylazar.ftcontrol.panels.CorePanels
import dalvik.system.DexClassLoader

object PluginManager {
    val allPlugins = mutableMapOf<String, PanelsPlugin<*>>()
    val plugins: MutableMap<String, PanelsPlugin<*>>
        get() = allPlugins.filter { it.value.isEnabled }.toMutableMap()
    private val finder = ClassFinder()

    fun onRegister(corePanels: CorePanels) {
        val modContext = ModContext()
        plugins.forEach { (id, plugin) ->
            println("PANELS: Plugin pages count: ${plugin.pages.size}")
            try {
                plugin.onRegister(modContext)
                plugin.cachedPages = plugin.pages.map { it.toJson }
                println("PANELS: Registered plugin $id")
            } catch (e: Exception) {
                println("PANELS: Failed to register plugin $id, ${e.message}")
                e.printStackTrace()
            }
        }
        println("PANELS: Registered ${plugins.size} plugins.")
    }

    fun loadPlugins(context: Context) {
        println("PANELS: Loading plugins...")
        allPlugins.clear()
        finder.apkPath = context.packageCodePath

        val foundClasses = finder.getAllClasses

        println("PANELS: Found ${foundClasses.size} classes:")

        foundClasses.forEach {
            try {
                val clazz = Class.forName(it.className)

                val pack = clazz.`package`
                if (pack != null && pack.name.startsWith("com.bylazar.ftcontrol")) return@forEach

                if (PanelsPlugin::class.java.isAssignableFrom(clazz)) {
                    println("PANELS: Found plugin implementation: ${clazz.name}")
                    val constructor = clazz.getDeclaredConstructor()
                    val pluginInstance = constructor.newInstance() as PanelsPlugin<*>

                    val originalId = pluginInstance.id
                    var uniqueId = originalId
                    var suffix = 1

                    while (allPlugins.containsKey(uniqueId)) {
                        println("PANELS: Plugin ID '$originalId' is already registered. Renaming...")
                        uniqueId = "$originalId${suffix++}"
                    }

                    pluginInstance.id = uniqueId

                    pluginInstance.handleConfig(foundClasses)

                    allPlugins[uniqueId] = pluginInstance

                    println("PANELS: Successfully registered plugin: ${clazz.name} with ID '$uniqueId'")

                }
            } catch (e: NoClassDefFoundError) {
                println("PANELS: Skipping plugin '${it.className}' due to missing class: ${e.message}")
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                println("PANELS: Class not found: ${it.className}")
                e.printStackTrace()
            } catch (e: ClassCastException) {
                println("PANELS: Class does not implement PanelsPlugin: ${it.className}")
                e.printStackTrace()
            } catch (e: Exception) {
                println("PANELS: Unexpected error loading plugin class: ${it.className}")
                e.printStackTrace()
            } catch(t: Throwable) {
                println("PANELS: Throwable caught: ${t::class.simpleName} - ${t.message}")
                t.printStackTrace()
            }
        }

        println("PANELS: Plugins map contents: ${allPlugins.keys}")

        println("PANELS: Loaded ${allPlugins.size} plugins.")

    }
}