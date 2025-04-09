package lol.lazar.lazarkit.panels.configurables.variables

import lol.lazar.lazarkit.panels.json.GenericTypeJson
import java.lang.reflect.Field

open class SimpleVariable(
    open var className: String,
    open var reference: Field,
) : GenericVariable() {
    inner class SimpleManager: GenericManager(){
        override fun getValue(): Any? {
            reference.isAccessible = true
            return try {
                reference.get(null)
            } catch (e: Exception) {
                println("DASH: Could not get value for ${reference.name}: ${e.message}")
                null
            }
        }

        override fun setValue(value: String): Boolean {
            reference.isAccessible = true

            try {
                reference.set(null, value)
                return true
            } catch (e: Exception) {
                println("DASH: Could not set value for ${reference.name}: ${e.message}")
                return false
            }
        }
    }

    val manager = SimpleManager()

    override val toJsonType: GenericTypeJson
        get() = GenericTypeJson(
            id = manager.id,
            className = className,
            fieldName = reference.name,
            type = type,
            valueString = manager.getValue().toString(),
            newValueString = manager.getValue().toString(),
            possibleValues = null,
            customValues = null,
            isOpened = false,
        )

}

class NestedSimpleVariable(
    var className: String,
    var parentReference: GenericManager,
    var reference: Field,
) : GenericVariable() {
    inner class NestedManager: GenericManager(){
        override fun getValue(): Any? {
            reference.isAccessible = true
            return try {
                reference.get(parentReference.getValue())
            } catch (e: Exception) {
                println("DASH: Could not get value for ${reference.name}: ${e.message}")
                null
            }
        }

        override fun setValue(value: String): Boolean {
            reference.isAccessible = true

            try {
                reference.set(parentReference.getValue(), value)
                return true
            } catch (e: Exception) {
                println("DASH: Could not set value for ${reference.name}: ${e.message}")
                return false
            }
        }
    }

    val manager = NestedManager()

    override val toJsonType: GenericTypeJson
        get() = GenericTypeJson(
            id = manager.id,
            className = className,
            fieldName = reference.name,
            type = type,
            valueString = manager.getValue().toString(),
            newValueString = manager.getValue().toString(),
            possibleValues = null,
            customValues = null,
            isOpened = false,
        )

}