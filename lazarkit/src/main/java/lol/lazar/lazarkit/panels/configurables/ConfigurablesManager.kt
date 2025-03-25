package lol.lazar.lazarkit.panels.configurables

import android.content.Context
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.ZipFile

class ConfigurablesManager {
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

    private val allClasses: List<String> by lazy {
        mutableListOf<String>().apply {
            try {
                val apkPath = context.packageCodePath
                ZipFile(apkPath).use { zipFile ->
                    val entries = zipFile.entries()
                    while (entries.hasMoreElements()) {
                        val entry = entries.nextElement()
                        if (entry.name.startsWith("classes") && entry.name.endsWith(".dex")) {
                            zipFile.getInputStream(entry).use { inputStream ->
                                val dexBytes = inputStream.readBytes()
                                val dexBuffer =
                                    ByteBuffer.wrap(dexBytes).order(ByteOrder.LITTLE_ENDIAN)
                                val classNames = extractClassNamesFromDex(dexBuffer)
                                addAll(classNames.filter { className ->
                                    ignored.none { prefix -> className.startsWith(prefix) }
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

    private lateinit var context: Context

    val getAllClasses: List<String>
        get() = allClasses

    fun findConfigurables(context: Context) {
        this.context = context
        getAllClasses.forEach { className ->
            println("DASH: $className")
        }
    }

    private fun extractClassNamesFromDex(dexBuffer: ByteBuffer): List<String> {
        val classNames = ArrayList<String>()

        dexBuffer.position(0)

        // Check DEX magic and version
        val magic = ByteArray(8)
        dexBuffer.get(magic)
        val magicString = String(magic)
        if (!magicString.startsWith("dex\n") || magicString.length < 8) {
            throw IllegalArgumentException("Invalid DEX magic: $magicString")
        }

        // Read header values with proper unsigned handling
        dexBuffer.position(32)
        val fileSize = dexBuffer.getInt().toLong() and 0xFFFFFFFFL

        dexBuffer.position(36)
        val headerSize = dexBuffer.getInt().toLong() and 0xFFFFFFFFL

        dexBuffer.position(56)
        val stringIdsSize = dexBuffer.getInt().toLong() and 0xFFFFFFFFL
        val stringIdsOffset = dexBuffer.getInt().toLong() and 0xFFFFFFFFL

        dexBuffer.position(64)
        val typeIdsSize = dexBuffer.getInt().toLong() and 0xFFFFFFFFL
        val typeIdsOffset = dexBuffer.getInt().toLong() and 0xFFFFFFFFL

        // Validate sizes and offsets
        if (stringIdsSize <= 0 || typeIdsSize <= 0 ||
            stringIdsOffset < headerSize || typeIdsOffset < headerSize ||
            stringIdsOffset > fileSize || typeIdsOffset > fileSize
        ) {
            throw IllegalArgumentException("Invalid table sizes or offsets: stringIdsSize=$stringIdsSize, typeIdsSize=$typeIdsSize, stringIdsOffset=$stringIdsOffset, typeIdsOffset=$typeIdsOffset")
        }

        // Read string offsets
        dexBuffer.position(stringIdsOffset.toInt())
        val stringOffsets = LongArray(stringIdsSize.toInt())
        for (i in 0 until stringIdsSize.toInt()) {
            stringOffsets[i] = dexBuffer.getInt().toLong() and 0xFFFFFFFFL
        }

        // Extract class names
        dexBuffer.position(typeIdsOffset.toInt())
        for (i in 0 until typeIdsSize.toInt()) {
            val stringIndex = dexBuffer.getInt().toLong() and 0xFFFFFFFFL
            if (stringIndex >= stringIdsSize) continue

            val stringOffset = stringOffsets[stringIndex.toInt()]
            val originalPosition = dexBuffer.position()

            try {
                dexBuffer.position(stringOffset.toInt())
                val length = readUleb128(dexBuffer)
                if (length > 1024) continue // Sanity check

                val stringBytes = ByteArray(length)
                dexBuffer.get(stringBytes)
                val classDescriptor = String(stringBytes)

                if (classDescriptor.startsWith("L") && classDescriptor.endsWith(";")) {
                    val className = classDescriptor
                        .substring(1, classDescriptor.length - 1)
                        .replace('/', '.')
                    classNames.add(className)
                }
            } catch (e: Exception) {
                println("DASH: Error processing class descriptor at index $i: ${e.message}")
            } finally {
                dexBuffer.position(originalPosition)
            }
        }

        return classNames
    }

    private fun readUleb128(buffer: ByteBuffer): Int {
        var result = 0
        var shift = 0
        var byte: Int

        do {
            byte = buffer.get().toInt() and 0xFF
            result = result or ((byte and 0x7F) shl shift)
            shift += 7
        } while (byte and 0x80 != 0 && shift < 35) // Prevent infinite loop

        return result
    }
}