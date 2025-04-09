package lol.lazar.lazarkit.panels.oldConfs

import lol.lazar.lazarkit.panels.oldConfs.annotations.Configurable
import lol.lazar.lazarkit.panels.oldConfs.annotations.ConfigurableCustomType
import lol.lazar.lazarkit.panels.oldConfs.annotations.IgnoreConfigurable
import lol.lazar.lazarkit.panels.oldConfs.utils.extractClassNamesFromDex
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.ZipFile

class ClassFinder {
    private val ignored: Set<String> = HashSet(
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
        val isConfigurable: Boolean = false,
        val isCustomType: Boolean = false,
    )

    lateinit var apkPath: String

    private val allClasses: List<ClassEntry> by lazy {
        mutableListOf<ClassEntry>().apply {
            try {
                ZipFile(apkPath).use { zipFile ->
                    val entries = zipFile.entries()
                    while (entries.hasMoreElements()) {
                        val entry = entries.nextElement()
                        if (entry.name.startsWith("classes") && entry.name.endsWith(".dex")) {
                            zipFile.getInputStream(entry).use { inputStream ->
                                val dexBytes = inputStream.readBytes()
                                val dexBuffer =
                                    ByteBuffer.wrap(dexBytes).order(ByteOrder.LITTLE_ENDIAN)
                                val classNames = dexBuffer.extractClassNamesFromDex()
                                val filteredClassNames = classNames.filter { className ->
                                    val isNotIgnored =
                                        ignored.none { prefix -> className.startsWith(prefix) }
                                    return@filter isNotIgnored
                                }
                                val processedClassNames = filteredClassNames.mapNotNull {
                                    try {
                                        val clazz = Class.forName(it)
                                        val hasConfigurable =
                                            clazz.isAnnotationPresent(Configurable::class.java)
                                        val hasIgnoreConfigurable =
                                            clazz.isAnnotationPresent(IgnoreConfigurable::class.java)
                                        val hasCustomType =
                                            clazz.isAnnotationPresent(ConfigurableCustomType::class.java)
                                        val shouldKeep =
                                            (hasConfigurable || hasCustomType) && !hasIgnoreConfigurable
                                        if (!shouldKeep) {
                                            return@mapNotNull null
                                        }
                                        ClassEntry(
                                            className = it,
                                            isConfigurable = hasConfigurable,
                                            isCustomType = hasCustomType,
                                        )
                                    } catch (e: ClassNotFoundException) {
                                        println("DASH: Class not found: $it")
                                        null
                                    } catch (e: Exception) {
                                        println("DASH: Error loading class $it: ${e.message}")
                                        null
                                    }
                                }
                                addAll(processedClassNames)
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                println("DASH: IOException occurred: ${e.message}")
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                println("DASH: IllegalArgumentException occurred: ${e.message}")
                e.printStackTrace()
            }
        }
    }


    val getAllClasses: List<ClassEntry>
        get() = allClasses

    val configurableClasses: List<ClassEntry>
        get() = allClasses.filter { it.isConfigurable }

    val customTypeClasses: List<ClassEntry>
        get() = allClasses.filter { it.isCustomType }
}