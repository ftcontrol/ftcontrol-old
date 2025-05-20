package com.bylazar.ftcontrol.panels.configurables

import com.bylazar.ftcontrol.panels.configurables.annotations.IgnoreConfigurable
import com.bylazar.ftcontrol.panels.configurables.variables.generics.GenericField
import java.lang.reflect.Modifier

class VariablesFinder(
    private val allClasses: () -> List<ClassFinder.ClassEntry>
) {

    private val jvmFields: List<GenericField> by lazy {
        mutableListOf<GenericField>().apply {
            allClasses().forEach { entry ->
                try {
                    val clazz = Class.forName(entry.className)
                    addFieldsFromClass(clazz, entry.className)
                    try {
                        val companionClazz = Class.forName("${entry.className}\$Companion")
                        addFieldsFromClass(companionClazz, entry.className)
                    } catch (e: ClassNotFoundException) {
                        // no companion found
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
        fields.forEach { field ->
            val annotations = field.annotations.map { it.toString() }

            val isFinal = Modifier.isFinal(field.modifiers)
            val isStatic = Modifier.isStatic(field.modifiers)
            val isIgnored = field.isAnnotationPresent(IgnoreConfigurable::class.java)

//            val isPrivate = Modifier.isPrivate(field.modifiers)
//            val isNull = try {
//                if (field.get(null) == null) {
//                    println("DASH: Field ${field.name} in $clazz is null")
//                    true
//                } else false
//            }catch (t: Throwable){
//                false
//            }

            val isJvmField = !isFinal && isStatic && !isIgnored

            if (isJvmField) {
                val displayClassName =
                    if (clazz.name.endsWith("\$Companion")) originalClassName else clazz.name
                val genericField = GenericField(className = displayClassName, reference = field)
                add(genericField)
            }
        }
    }

    val getJvmFields: List<GenericField>
        get() = jvmFields
}