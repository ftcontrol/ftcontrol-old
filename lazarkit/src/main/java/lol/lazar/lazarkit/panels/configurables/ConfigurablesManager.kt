package lol.lazar.lazarkit.panels.configurables

import android.content.Context

class ConfigurablesManager {
    var finder = ClassFinder()
    var variables = VariablesManager({ finder.getAllClasses })

    fun findConfigurables(context: Context) {
        finder.apkPath = context.packageCodePath
        println("DASH: Found ${finder.getAllClasses.size} configurable classes:")
        finder.getAllClasses.forEach { className ->
            println("DASH: $className")
        }

        println("DASH: Found ${variables.getJvmFields.size} @JvmField variables:")
        variables.getJvmFields.forEach { info ->
            println("DASH: ${info.className}.${info.field.name} = ${info.currentValue}")
        }
    }
}