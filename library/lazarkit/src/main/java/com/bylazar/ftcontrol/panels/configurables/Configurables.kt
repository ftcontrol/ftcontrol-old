package com.bylazar.ftcontrol.panels.configurables

import android.content.Context
import com.bylazar.ftcontrol.panels.configurables.variables.MyField
import com.bylazar.ftcontrol.panels.configurables.variables.generics.GenericField

object Configurables {
    var fieldsMap = mutableMapOf<String, MyField>()
    var configurableClasses: List<ClassFinder.ClassEntry> = listOf()

    var jvmFields = listOf<GenericField>()

    private var finder = ClassFinder()
    private var variables = VariablesFinder({ finder.getAllClasses })

    fun findConfigurables(context: Context) {
        fieldsMap = mutableMapOf()
        configurableClasses = listOf()
        jvmFields = listOf()

        finder.apkPath = context.packageCodePath
        println("DASH: Found ${finder.getAllClasses.size} configurable classes:")
        finder.getAllClasses.forEach { className ->
            println("DASH: $className")
        }
        println("DASH: Found ${variables.getJvmFields.size} @JvmField variables:")
        variables.getJvmFields.forEach { info ->
            println("DASH: ${info.className}.${info.reference.name}")
            info.debug()
        }
        jvmFields = variables.getJvmFields
    }
}