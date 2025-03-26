package lol.lazar.lazarkit.panels.configurables

import lol.lazar.lazarkit.panels.data.JvmFieldInfoArray
import lol.lazar.lazarkit.panels.data.JvmFieldInfoBase
import lol.lazar.lazarkit.panels.data.JvmFieldInfoDouble
import lol.lazar.lazarkit.panels.data.JvmFieldInfoInt
import lol.lazar.lazarkit.panels.data.JvmFieldInfoString
import lol.lazar.lazarkit.panels.data.JvmFieldInfoUnknown
import java.lang.reflect.Field
import java.lang.reflect.Modifier

class VariablesManager(
    private val allClasses: () -> List<String>
) {
    data class JvmFieldInfo(
        val className: String,
        val field: Field,
        val currentValue: Any?
    ) {
        fun toJsonType(): JvmFieldInfoBase {
            when (this.field.type) {
                Int::class.java -> {
                    return JvmFieldInfoInt(
                        className = this.className,
                        fieldName = this.field.name,
                        currentValue = (this.currentValue as Int)
                    )
                }

                String::class.java -> {
                    return JvmFieldInfoString(
                        className = this.className,
                        fieldName = this.field.name,
                        currentValue = (this.currentValue as String)
                    )
                }

                Double::class.java -> {
                    return JvmFieldInfoDouble(
                        className = this.className,
                        fieldName = this.field.name,
                        currentValue = (this.currentValue as Double)
                    )
                }

                else -> {
                    if (this.field.type.isArray) {
                        val componentType = field.type.componentType
                        val values = when (componentType) {
                            Int::class.java -> (currentValue as IntArray).map {
                                JvmFieldInfoInt(
                                    className,
                                    field.name,
                                    it
                                )
                            }

                            Double::class.java -> (currentValue as DoubleArray).map {
                                JvmFieldInfoDouble(
                                    className,
                                    field.name,
                                    it
                                )
                            }

                            String::class.java -> (currentValue as Array<String>).map {
                                JvmFieldInfoString(
                                    className,
                                    field.name,
                                    it
                                )
                            }

                            else -> {
                                emptyList()
                            }
                        }

                        return JvmFieldInfoArray(className, field.name, values)
                    }
                }
            }

            println("DASH: Unknown field type: ${this.field.type.name}")
            return JvmFieldInfoUnknown(
                className = this.className,
                fieldName = this.field.name,
            )
        }
    }

    private val jvmFields: List<JvmFieldInfo> by lazy {
        mutableListOf<JvmFieldInfo>().apply {
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

    private fun MutableList<JvmFieldInfo>.addFieldsFromClass(
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

            var isJvmField = !isFinal && isStatic

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
                add(JvmFieldInfo(displayClassName, field, currentValue))
            }
        }
    }

    val getJvmFields: List<JvmFieldInfo>
        get() = jvmFields
}