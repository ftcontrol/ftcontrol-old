package lol.lazar.lazarkit.panels.configurables

import android.content.Context
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

    lateinit var apkPath: String

    private val allClasses: List<String> by lazy {
        mutableListOf<String>().apply {
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
                                addAll(classNames.filter { className ->
                                    val isNotIgnored =
                                        ignored.none { prefix -> className.startsWith(prefix) }
                                    if (!isNotIgnored) return@filter false

                                    try {
                                        val clazz = Class.forName(className)
                                        val hasConfigurable =
                                            clazz.isAnnotationPresent(Configurable::class.java)
                                        val hasIgnoreConfigurable =
                                            clazz.isAnnotationPresent(IgnoreConfigurable::class.java)
                                        hasConfigurable && !hasIgnoreConfigurable
                                    } catch (e: ClassNotFoundException) {
                                        println("DASH: Class not found: $className")
                                        false
                                    } catch (e: Exception) {
                                        println("DASH: Error loading class $className: ${e.message}")
                                        false
                                    }
                                })
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

    val getAllClasses: List<String>
        get() = allClasses
}