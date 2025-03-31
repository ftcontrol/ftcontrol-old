package lol.lazar.lazarkit.panels.configurables

import android.content.Context
import android.os.Handler
import android.os.Looper
import kotlin.random.Random

class ConfigurablesManager {
    var finder = ClassFinder()
    var variables = VariablesFinder({ finder.getAllClasses })
    private val handler = Handler(Looper.getMainLooper())


    fun findConfigurables(context: Context) {
        finder.apkPath = context.packageCodePath
        println("DASH: Found ${finder.configurableClasses.size} configurable classes:")
        finder.configurableClasses.forEach { className ->
            println("DASH: $className")
        }
        GlobalFields.configurableClasses = finder.configurableClasses
        println("DASH: Found ${finder.customTypeClasses.size} custom type classes:")
        finder.customTypeClasses.forEach { className ->
            println("DASH: $className")
        }
        GlobalFields.customTypeClasses = finder.customTypeClasses
        println("DASH: Found ${variables.getJvmFields.size} @JvmField variables:")
        variables.getJvmFields.forEach { info ->
            println("DASH: ${info.className}.${info.reference.name} = ${info.currentValue}")
        }

        GlobalFields.jvmFields = variables.getJvmFields

//        startRandomUpdates()
    }

    private fun startRandomUpdates() {
        handler.post(object : Runnable {
            override fun run() {
                updateIntegerVariables()
                handler.postDelayed(this, 2000) // Run every 2 seconds
            }
        })
    }

    private fun updateIntegerVariables() {
        variables.getJvmFields.forEach { info ->
            if (info.reference.type == Int::class.java) {
                try {
                    val randomValue = Random.nextInt(0, 100) // Generate a random number
                    info.reference.set(null, randomValue)
                    println("DASH: Updated ${info.className}.${info.reference.name} = $randomValue")
                } catch (e: Exception) {
                    println("DASH: Failed to update ${info.className}.${info.reference.name}: ${e.message}")
                }
            }
        }
    }
}