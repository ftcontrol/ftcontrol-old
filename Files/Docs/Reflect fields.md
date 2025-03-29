import kotlin.reflect.full.memberProperties
import java.lang.reflect.Field

inline fun <reified T> getField(value: T, fieldName: String): Field? {
    return T::class.java.getDeclaredField(fieldName)?.apply {
        isAccessible = true // Allow access to private fields if needed
    }
}

data class Person(val name: String, val age: Int)

fun main() {
    val person = Person("Alice", 25)

    // Get the field by name
    val field = getField(person, "name")

    // Get the field's value
    val fieldValue = field?.get(person)
    
    println("Field: $field")
    println("Value: $fieldValue")
}
