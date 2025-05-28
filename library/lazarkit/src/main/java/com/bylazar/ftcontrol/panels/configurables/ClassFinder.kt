package com.bylazar.ftcontrol.panels.configurables

import com.bylazar.ftcontrol.panels.Logger
import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable
import com.bylazar.ftcontrol.panels.configurables.annotations.IgnoreConfigurable
import com.bylazar.ftcontrol.panels.configurables.utils.extractClassNamesFromDex
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.ZipFile

class ClassFinder {
    private val ignoredPackages: Set<String> = HashSet(
        mutableListOf(
            "com.google",
            "com.sun",
            "sun",
            "com.qualcomm",
            "org.opencv",
            "java",
            "javax",
            "android",
            "com.android",
            "kotlin",
            "dalvik",
            "org.firstinspires.ftc.robotcore",
            "org.intellij",
            "org.firstinspires.inspection",
            "org.firstinspires.ftc.robotserver",
            "org.firstinspires.ftc.ftccommon",
            "org.firstinspires.ftc.robotcontroller",
            "org.firstinspires.ftc.onbotjava",
            "org.firstinspires.ftc.vision",
            "org.firstinspires.ftc.apriltag",
            "org.slf4j",
            "org.threeten",
            "org.w3c",
            "org.xmlpull",
            "org.java_websocket",
            "fi.iki",
            "okhttp3",
            "com.journeyapps",
            "dk.sgjesse",
            "org.openjsse",
            "com.jakewharton",
            "org.openftc",
            "jdk.Exported",
            "org.json",
            "org.xml",
            "nl.minvws",
            "okio",
            "org.bouncycastle",
            "org.conscrypt",
            "org.jetbrains"
        )
    )

    data class ClassEntry(
        val className: String,
    )

    fun updateClasses(apkPath: String) {
        var newClasses = mutableListOf<ClassEntry>()
        try {
            ZipFile(apkPath).use { zipFile ->
                val entries = zipFile.entries()
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    if (entry.name.startsWith("classes") && entry.name.endsWith(".dex")) {
                        Logger.configurablesLog("Checking ${entry.name}")
                        zipFile.getInputStream(entry).use { inputStream ->
                            val dexBytes = inputStream.readBytes()
                            val dexBuffer =
                                ByteBuffer.wrap(dexBytes).order(ByteOrder.LITTLE_ENDIAN)
                            val classNames = dexBuffer.extractClassNamesFromDex()
                            val filteredClassNames = classNames.filter { className ->
                                val isNotIgnored =
                                    ignoredPackages.none { prefix -> className.startsWith(prefix) }
                                return@filter isNotIgnored
                            }
                            Logger.configurablesLog("Found ${filteredClassNames.size} classes in ${entry.name}")
                            val processedClassNames = filteredClassNames.mapNotNull {
                                try {
                                    val clazz = Class.forName(it)
                                    val hasConfigurable =
                                        clazz.isAnnotationPresent(Configurable::class.java)
                                    val hasIgnoreConfigurable =
                                        clazz.isAnnotationPresent(IgnoreConfigurable::class.java)
                                    val shouldKeep =
                                        hasConfigurable && !hasIgnoreConfigurable
                                    if (!shouldKeep) {
                                        return@mapNotNull null
                                    }
                                    ClassEntry(
                                        className = it,
                                    )
                                } catch (e: ClassNotFoundException) {
                                    Logger.configurablesError("Class not found: $it")
                                    null
                                } catch (e: Exception) {
                                    Logger.configurablesError("Error loading class $it: ${e.message}")
                                    null
                                } catch (t: Throwable) {
                                    Logger.configurablesError("Throwable caught: ${t::class.simpleName} - ${t.message}")
                                    null
                                }
                            }
                            Logger.configurablesLog("Found ${processedClassNames.size} processed classes in ${entry.name}")
                            newClasses.addAll(processedClassNames)
                        }
                    }
                }
            }
        } catch (e: IOException) {
            println("PANELS: CONFIGURABLES: IOException occurred: ${e.message}")
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            println("PANELS: CONFIGURABLES: IllegalArgumentException occurred: ${e.message}")
            e.printStackTrace()
        } catch (t: Throwable) {
            println("PANELS: CONFIGURABLES: C Throwable caught: ${t::class.simpleName} - ${t.message}")
            t.printStackTrace()
        }
        classes = newClasses
    }


    var classes: List<ClassEntry> = listOf()
}