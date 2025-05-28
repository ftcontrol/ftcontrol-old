package com.bylazar.ftcontrol.panels.configurables

import com.bylazar.ftcontrol.panels.Logger
import com.bylazar.ftcontrol.panels.configurables.annotations.IgnoreConfigurable
import com.bylazar.ftcontrol.panels.configurables.variables.generics.GenericField
import java.lang.reflect.Modifier

class VariablesFinder() {

    fun updateJvmFields(allClasses: List<ClassFinder.ClassEntry>) {
        val output = mutableListOf<GenericField>()
        allClasses.forEach { entry ->
            try {
                val clazz = Class.forName(entry.className)
                output.addFieldsFromClass(clazz, entry.className)
                try {
                    val companionClazz = Class.forName("${entry.className}\$Companion")
                    output.addFieldsFromClass(companionClazz, entry.className)
                } catch (e: ClassNotFoundException) {
                    // no companion found
                }
            } catch (e: Exception) {
                Logger.configurablesError("Error inspecting class ${entry.className}: ${e.message}")
            }
        }
        jvmFields = output
    }


    private val excludedPackages: Set<String> = HashSet(
        mutableListOf(
            "com.google",
            "com.sun",
            "sun",
            "com.qualcomm",
            "org.opencv",
            "android",
            "com.android",
            "dalvik",
            "org.intellij",
            "org.slf4j",
            "org.threeten",
            "org.w3c",
            "org.xmlpull",
            "org.java_websocket",
            "com.journeyapps",
            "dk.sgjesse",
            "com.jakewharton",
            "org.openftc",
            "org.xml",
            "org.jetbrains",
            "org.firstinspires.ftc.robotcore",
            "org.firstinspires.inspection",
            "org.firstinspires.ftc.robotserver",
            "org.firstinspires.ftc.ftccommon",
            "org.firstinspires.ftc.robotcontroller",
            "org.firstinspires.ftc.onbotjava",
            "org.firstinspires.ftc.vision",
            "org.firstinspires.ftc.apriltag",
        )
    )

    private fun MutableList<GenericField>.addFieldsFromClass(
        clazz: Class<*>,
        originalClassName: String
    ) {
        val fields = clazz.declaredFields
        fields.forEach { field ->
            try {
                val annotations = field.annotations.map { it.toString() }

                val isFinal = Modifier.isFinal(field.modifiers)
                val isStatic = Modifier.isStatic(field.modifiers)
                val isIgnored = field.isAnnotationPresent(IgnoreConfigurable::class.java)

//            val isPrivate = Modifier.isPrivate(field.modifiers)
//            val isNull = try {
//                if (field.get(null) == null) {
//                    println("PANELS: Field ${field.name} in $clazz is null")
//                    true
//                } else false
//            }catch (t: Throwable){
//                false
//            }

                val isJvmField = !isFinal && isStatic && !isIgnored

                val fieldTypeName = field.type.canonicalName ?: ""
                val isInExcludedPackage = excludedPackages.any { fieldTypeName.startsWith(it) }

                Logger.configurablesLog("Field of $fieldTypeName / $isJvmField / $isInExcludedPackage / shown: ${isJvmField && !isInExcludedPackage}")

                if (isJvmField && !isInExcludedPackage) {
                    val displayClassName =
                        if (clazz.name.endsWith("\$Companion")) originalClassName else clazz.name
                    val genericField = GenericField(className = displayClassName, reference = field)
                    Logger.configurablesLog("Adding field $genericField / ${genericField.type} / ${genericField.value} / ${genericField.isNull}")
                    add(genericField)
                }
            } catch (t: Throwable) {
                Logger.configurablesError("Error inspecting field ${field.name} in $clazz: ${t.message}")
            }
        }
    }

    var jvmFields: List<GenericField> = listOf()
}