package lol.lazar.lazarkit.panels.configurables

import android.content.Context
import lol.lazar.lazarkit.panels.configurables.variables.generics.GenericField
import lol.lazar.lazarkit.panels.configurables.variables.generics.GenericManager

object Configurables {
    var fieldsMap = mutableMapOf<String, GenericManager>()
    var configurableClasses: List<ClassFinder.ClassEntry> = listOf()
    var customTypeClasses: List<ClassFinder.ClassEntry> = listOf()

    var jvmFields = listOf<GenericField>()

    private var finder = ClassFinder()
    private var variables = VariablesFinder({ finder.getAllClasses })

    fun findConfigurables(context: Context) {
        fieldsMap = mutableMapOf()
        configurableClasses = listOf()
        customTypeClasses = listOf()
        jvmFields = listOf()

        finder.apkPath = context.packageCodePath
        println("DASH: Found ${finder.configurableClasses.size} configurable classes:")
        finder.configurableClasses.forEach { className ->
            println("DASH: $className")
        }
        configurableClasses = finder.configurableClasses
        println("DASH: Found ${finder.customTypeClasses.size} custom type classes:")
        finder.customTypeClasses.forEach { entry ->
            println("DASH: ${entry.className} / ${Class.forName(entry.className).declaredFields.map { it.name }}")
        }
        customTypeClasses = finder.customTypeClasses
        println("DASH: Found ${variables.getJvmFields.size} @JvmField variables:")
        variables.getJvmFields.forEach { info ->
            println("DASH: ${info.className}.${info.reference.name}")
            info.debug()
        }
        jvmFields = variables.getJvmFields
    }
}