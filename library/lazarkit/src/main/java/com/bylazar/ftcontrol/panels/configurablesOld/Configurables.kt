package com.bylazar.ftcontrol.panels.configurablesOld

import android.content.Context
import com.bylazar.ftcontrol.panels.configurablesOld.variables.MyField
import com.bylazar.ftcontrol.panels.configurablesOld.variables.generics.GenericField
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
            println("PANELS: CONFIGURABLES: Searching for configurables classes...")
            finder.apkPath = context.packageCodePath
            println("PANELS: CONFIGURABLES: Found ${finder.getAllClasses.size} configurable classes:")
            finder.getAllClasses.forEach { className ->
                println("PANELS: CONFIGURABLES: $className")
            }
        }catch (t: Throwable){
            println("PANELS: CONFIGURABLES: ClassFinder Throwable caught: ${t::class.simpleName} - ${t.message}")
            t.printStackTrace()
        }

        try {
            println("PANELS: CONFIGURABLES: Searching for configurables variables...")
            println("PANELS: CONFIGURABLES: Found ${variables.getJvmFields.size} @JvmField variables:")
            variables.getJvmFields.forEach { info ->
                println("PANELS: CONFIGURABLES: ${info.className}.${info.reference.name}")
                info.debug()
            }
        }catch (t: Throwable){
            println("PANELS: CONFIGURABLES: VariablesFinder Throwable caught: ${t::class.simpleName} - ${t.message}")
            t.printStackTrace()
        }

        try {
            println("PANELS: CONFIGURABLES: Searching for json...")
            jvmFields = variables.getJvmFields
            initialJvmFields = jvmFields.map { it.toJsonType }
        }catch (t: Throwable){
            println("PANELS: CONFIGURABLES: JsonFinder Throwable caught: ${t::class.simpleName} - ${t.message}")
            t.printStackTrace()
        }
    }
}