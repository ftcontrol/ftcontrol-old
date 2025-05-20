package com.bylazar.ftcontrol.panels.configurables

import android.content.Context
import com.bylazar.ftcontrol.panels.configurables.variables.MyField
import com.bylazar.ftcontrol.panels.configurables.variables.generics.GenericField
import com.bylazar.ftcontrol.panels.json.GenericTypeJson

object Configurables {
    var fieldsMap = mutableMapOf<String, MyField>()
    var configurableClasses: List<ClassFinder.ClassEntry> = listOf()

    var jvmFields = listOf<GenericField>()
    var initialJvmFields = listOf<GenericTypeJson>()

    private var finder = ClassFinder()
    private var variables = VariablesFinder({ finder.getAllClasses })

    fun findConfigurables(context: Context) {
        fieldsMap = mutableMapOf()
        configurableClasses = listOf()
        jvmFields = listOf()

        try{
            println("DASH: Searching for configurables classes...")
            finder.apkPath = context.packageCodePath
            println("DASH: Found ${finder.getAllClasses.size} configurable classes:")
            finder.getAllClasses.forEach { className ->
                println("DASH: $className")
            }
        }catch (t: Throwable){
            println("DASH: ClassFinder Throwable caught: ${t::class.simpleName} - ${t.message}")
            t.printStackTrace()
        }

        try {
            println("DASH: Searching for configurables variables...")
            println("DASH: Found ${variables.getJvmFields.size} @JvmField variables:")
            variables.getJvmFields.forEach { info ->
                println("DASH: ${info.className}.${info.reference.name}")
                info.debug()
            }
        }catch (t: Throwable){
            println("DASH: VariablesFinder Throwable caught: ${t::class.simpleName} - ${t.message}")
            t.printStackTrace()
        }

        try {
            println("DASH: Searching for json...")
            jvmFields = variables.getJvmFields
            initialJvmFields = jvmFields.map { it.toJsonType }
        }catch (t: Throwable){
            println("DASH: JsonFinder Throwable caught: ${t::class.simpleName} - ${t.message}")
            t.printStackTrace()
        }
    }
}