package com.bylazar.ftcontrol.panels.plugins

import android.content.Context
import com.bylazar.ftcontrol.panels.configurables.utils.extractClassNamesFromDex
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteOrder
import java.nio.channels.FileChannel

object Helpers {
    fun loadPlugins(context: Context) {
        println("DASH: Length: ${context.assets.list("plugins")?.size}")
        context.assets.list("plugins")?.forEach { pluginName ->
            println("DASH: Found plugin in assets: $pluginName")

            try {
                val tempFile = File.createTempFile("plugin_", ".dex", context.cacheDir)
                context.assets.open("plugins/$pluginName").use { input ->
                    FileOutputStream(tempFile).use { output ->
                        input.copyTo(output)
                    }
                }

                val classLoader = DexClassLoader(
                    tempFile.absolutePath,
                    context.cacheDir.absolutePath,
                    null,
                    context.classLoader
                )

                val byteBuffer = FileInputStream(tempFile).channel.use { channel ->
                    channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()).apply {
                        order(ByteOrder.LITTLE_ENDIAN)
                    }
                }

                val classNames = byteBuffer.extractClassNamesFromDex()
                println("DASH: Found classes in $pluginName: $classNames")

                classNames.forEach { className ->
                    try {
                        val pluginClass = classLoader.loadClass(className)

                        if (PanelsPlugin::class.java.isAssignableFrom(pluginClass)) {
                            println("DASH: Found plugin implementation: $className")

                            val pluginInstance = pluginClass.getDeclaredConstructor().newInstance() as PanelsPlugin
                            pluginInstance.onRegister()
                            println("DASH: Successfully registered plugin: $className")
                        }
                    } catch (e: Exception) {
                        println("DASH: Error processing class $className: ${e.message}")
                        e.printStackTrace()
                    }
                }

                tempFile.delete()
            } catch (e: Exception) {
                e.printStackTrace()
                println("DASH: Error loading plugin $pluginName: ${e.message}")
            }
        }
    }
}