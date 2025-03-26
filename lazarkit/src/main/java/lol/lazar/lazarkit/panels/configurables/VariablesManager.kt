package lol.lazar.lazarkit.panels.configurables

import java.lang.reflect.Modifier

class VariablesManager(
    private val allClasses: () -> List<String>
) {

    private val jvmFields: List<GenericType> by lazy {
        mutableListOf<GenericType>().apply {
            allClasses().forEach { className ->
                try {
                    val clazz = Class.forName(className)
                    println("DASH: Inspecting outer class: $className")
                    addFieldsFromClass(clazz, className)
                    try {
                        val companionClazz = Class.forName("$className\$Companion")
                        println("DASH: Found and inspecting Companion class: $className\$Companion")
                        addFieldsFromClass(companionClazz, className)
                    } catch (e: ClassNotFoundException) {
                        println("DASH: No Companion class found for $className")
                    }
                } catch (e: Exception) {
                    println("DASH: Error inspecting class $className: ${e.message}")
                }
            }
        }
    }

    private fun MutableList<GenericType>.addFieldsFromClass(
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

            println("DASH: Is ${field.name} final? $isFinal")
            println("DASH: Is ${field.name} static? $isStatic")

            val isJvmField = !isFinal && isStatic

            println("DASH: Is ${field.name} a JvmField? $isJvmField")
            if (isJvmField) {
                field.isAccessible = true
                val currentValue = try {
                    field.get(null)
                } catch (e: Exception) {
                    println("DASH: Could not get value for ${field.name}: ${e.message}")
                    null
                }
                val displayClassName =
                    if (clazz.name.endsWith("\$Companion")) originalClassName else clazz.name
                println("DASH: Adding JvmField: $displayClassName.${field.name} = $currentValue")
                add(GenericType(displayClassName, field, currentValue))
            }
        }
    }

    val getJvmFields: List<GenericType>
        get() = jvmFields
}