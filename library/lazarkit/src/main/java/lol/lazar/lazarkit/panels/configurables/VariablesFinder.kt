package lol.lazar.lazarkit.panels.configurables

import lol.lazar.lazarkit.panels.configurables.annotations.IgnoreConfigurable
import lol.lazar.lazarkit.panels.configurables.variables.GenericField
import java.lang.reflect.Modifier

class VariablesFinder(
    private val allClasses: () -> List<ClassFinder.ClassEntry>
) {

    private val jvmFields: List<GenericField> by lazy {
        mutableListOf<GenericField>().apply {
            allClasses().forEach { entry ->
                try {
                    val clazz = Class.forName(entry.className)
                    println("DASH: Inspecting outer class: ${entry.className}")
                    addFieldsFromClass(clazz, entry.className)
                    try {
                        val companionClazz = Class.forName("${entry.className}\$Companion")
                        println("DASH: Found and inspecting Companion class: ${entry.className}\$Companion")
                        addFieldsFromClass(companionClazz, entry.className)
                    } catch (e: ClassNotFoundException) {
                        println("DASH: No Companion class found for ${entry.className}")
                    }
                } catch (e: Exception) {
                    println("DASH: Error inspecting class ${entry.className}: ${e.message}")
                }
            }
        }
    }

    private fun MutableList<GenericField>.addFieldsFromClass(
        clazz: Class<*>,
        originalClassName: String
    ) {
        val fields = clazz.declaredFields
        println("DASH: Found ${fields.size} declared fields in ${clazz.name}")
        fields.forEach { field ->
            val annotations = field.annotations.map { it.toString() } // Full annotation details
            println("DASH: Checking field: ${field.name}, annotations: $annotations")

            val isFinal = Modifier.isFinal(field.modifiers)
            val isStatic = Modifier.isStatic(field.modifiers)
            val isIgnored = field.isAnnotationPresent(IgnoreConfigurable::class.java)

            println("DASH: Is ${field.name} final? $isFinal")
            println("DASH: Is ${field.name} static? $isStatic")

            val isJvmField = !isFinal && isStatic && !isIgnored

            println("DASH: Is ${field.name} a JvmField? $isJvmField")
            if (isJvmField) {
                val displayClassName =
                    if (clazz.name.endsWith("\$Companion")) originalClassName else clazz.name
                val genericField = GenericField(className = displayClassName, reference = field)
                println("DASH: Adding JvmField: ${genericField.className}.${genericField.name} = ${genericField.currentValue}")
                add(genericField)
            }
        }
    }

    val getJvmFields: List<GenericField>
        get() = jvmFields
}