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
            try {
                plugin.onRegister(modContext)
            } catch (e: Exception) {
                println("DASH: Failed to register plugin $id, ${e.message}")
                e.printStackTrace()
            }
        }
        println("DASH: Registered ${plugins.size} plugins.")
    }

    fun loadPlugins(context: Context) {
        println("DASH: Loading plugins...")
        allPlugins.clear()
        finder.apkPath = context.packageCodePath

        val foundClasses = finder.getAllClasses

        println("DASH: Found ${foundClasses.size} classes:")

        foundClasses.forEach {
            try {
                val clazz = Class.forName(it.className)

                val pack = clazz.`package`
                if (pack != null && pack.name.startsWith("com.bylazar.ftcontrol")) return@forEach

                if (PanelsPlugin::class.java.isAssignableFrom(clazz)) {
                    println("DASH: Found plugin implementation: ${clazz.name}")
                    val constructor = clazz.getDeclaredConstructor()
                    val pluginInstance = constructor.newInstance() as PanelsPlugin<*>

                    val originalId = pluginInstance.id
                    var uniqueId = originalId
                    var suffix = 1

                    while (allPlugins.containsKey(uniqueId)) {
                        println("DASH: Plugin ID '$originalId' is already registered. Renaming...")
                        uniqueId = "$originalId${suffix++}"
                    }

                    pluginInstance.id = uniqueId

                    pluginInstance.handleConfig(foundClasses)

                    allPlugins[uniqueId] = pluginInstance

                    println("DASH: Successfully registered plugin: ${clazz.name} with ID '$uniqueId'")

                }
            } catch (e: NoClassDefFoundError) {
                println("DASH: Skipping plugin '${it.className}' due to missing class: ${e.message}")
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                println("DASH: Class not found: ${it.className}")
                e.printStackTrace()
            } catch (e: ClassCastException) {
                println("DASH: Class does not implement PanelsPlugin: ${it.className}")
                e.printStackTrace()
            } catch (e: Exception) {
                println("DASH: Unexpected error loading plugin class: ${it.className}")
                e.printStackTrace()
            } catch(t: Throwable) {
                println("DASH: Throwable caught: ${t::class.simpleName} - ${t.message}")
                t.printStackTrace()
            }
        }

        println("DASH: Plugins map contents: ${allPlugins.keys}")

        println("DASH: Loaded ${allPlugins.size} plugins.")

    }
}