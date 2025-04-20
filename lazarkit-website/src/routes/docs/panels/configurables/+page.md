# Configurables

Configurables are runtime-modifiable variables that make *testing*, *tuning*, and *debugging* robot behavior easier without needing to recompile or reupload your code. You can think of them as live sliders or inputs exposed to your Panels Dashboard.

The Configurables Widget also supports search functionality.

<video width="100%" controls>
  <source src="/docs/configurables_example.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>

## What is a Configurable?

A Configurable is a `static` (Java) or `@JvmField` (Kotlin) variable marked in a class annotated with `@Configurable`. These variables are exposed in the Panels UI and can be changed while the robot is running.

Useful for things like:
- PID tuning
- Autonomous positions and paths
- Behavioral toggles (e.g., enabling/disabling subsystems)
- Testing new constants on-the-fly

---

## Configurables UI Features

<img src="/docs/configurables_ui.png"/>

1. *Update current variable* – You can also use `ENTER` on supported types to update.
2. *Update all modified variables* – Apply changes to all variables that have been modified.
3. *Toggle Differential Mode* – Displays all modified variables.
4. *Search bar* – Allows searching by variable name.


---

## Annotating Classes with @Configurable

To make a class’s fields configurable:

```java
@Configurable
public class RobotConstants {
    public static int MAGIC_NUMBER = 32;
}
```

In Kotlin:

```kotlin
@Configurable
object RobotConstants {
    @JvmField
    var magicNumber = 32
}
```

---

## Real Examples

You can define configurables in many ways. Let's explore the styles supported.

1. Java Class

```java
@Configurable
public class TestJavaClass {
    public static int testInt = 1;
    public static boolean testBoolean = false;
    public static String testString = "Hello";
}

```

2. Kotlin Class

```kotlin
@Configurable
class TestKotlinClass {
    companion object {
        @JvmField
        var testDouble: Double = 1.0
    }
}
```

3. Kotlin Object

```kotlin
@Configurable
object TestKotlinObject {
    @JvmField
    var testFloat: Float = 1.0f
}
```

---

## Supported Types

All fields must be:
- public static (Java)
- @JvmField inside an object or companion object (Kotlin)
And must be non-final.

You can use:
- Primitive types: int, double, boolean, etc.
- Enums
- Strings
- Arrays and Lists
- Maps (read-only unless exposed via a custom dashboard)
- Custom types (detected automatically)
- Generic types (via @GenericValue)

---

## Custom Types
Custom objects are detected automatically.

```java
public class CustomType {
    public final int id;
    public final String name;
    
    public CustomType(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
```
You can nest custom types, too:
```kotlin
public class NestedType {
    public final CustomType child;

    public NestedType(CustomType child) {
        this.child = child;
    }
}
```

## Generic Types with @GenericValue
If you have a generic wrapper class:

```java
public class TParamClass<T> {
    public final T value;
    public TParamClass(T value) { this.value = value; }
}
```
Then mark the instance with its type:
```java
@GenericValue(tParam = Integer.class)
public static TParamClass<Integer> testGeneric = new TParamClass<>(42);
```
Kotlin:
```kotlin
@GenericValue(Int::class)
@JvmField
var testTParamClass = TParamClass(1)
```

---

## Gotchas

### Static-only

All configurable fields must be static or top-level @JvmFields. Instance fields won’t be tracked by the dashboard.

### Changes are One-Way

Updates on the dashboard side reflect immediately in your robot code.
But updates in code do not update the dashboard until a refresh is triggered.
This means:
- Editing a value in code does not update it live on the dashboard.
- You can overwrite dashboard changes if your code re-initializes the value.

---

## The Common Pitfall: Copy Semantics

In Kotlin and Java, when you assign an object to a new variable, you copy the reference  to the object, not the object itself. This means both variables point to the same memory location. However, when dealing with primitives (like int, double, etc.) or immutable objects, the value is copied directly. This can lead to unexpected behavior if one variable is modified, as the other variable won't reflect the change.

### Example: Implementing a configurable Claw System

### Bad Example

@code(/../test-codebase/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/examples/configurables/copysemantics/bad/Example.kt)

#### Problem:

In this example, the `offset` variable is passed as a value to the `RobotClaw` constructor during initialization. Since `Double` is a primitive type, its value is copied into the `RobotClaw` object. If `offset` is changed later during the OpMode (e.g., via Panels interface), the `RobotClaw` instance will not reflect the updated value. This leads to inconsistent behavior because the `RobotClaw` continues to use the old offset value.

### Solution 1: Use a Mutable Shared State

One way to solve this issue is to make the `clawOffset` a mutable shared state that can be updated dynamically. Instead of passing the value directly, we store it in a shared location (e.g., a `companion object`) and reference it within the `RobotClaw`.

@code(/../test-codebase/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/examples/configurables/copysemantics/fixed/Example.kt)

### Solution 2: Use a Lambda Function

Another elegant solution is to pass a `lambda function` that retrieves the current value of `offset`. This ensures that the `RobotClaw` always queries the latest value of `offset` whenever it needs it.

@code(/../test-codebase/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/examples/configurables/copysemantics/lambda/Example.kt)

---

## Playground: Random Test Arrays

You can even test the limits with things like:
```kotlin
@JvmField
var testRandomArray = arrayOf(
    1,
    1.0,
    1.0f,
    "test!",
    true,
    CustomType(1, "test!"),
    NestedType(1, "test!", CustomType(2, "test2!")),
    UnknownType(1),
    arrayOf(
        1,
        2,
        3
    ),
    mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3
    )
)
```
Note: only supported types will render correctly on the dashboard.

<img src="/docs/configurables_random_array.png"/>
---

## Best Practices
- Group related config values into logical classes like `DriveConstants`, `ArmConfig`, etc.
- Keep types simple. Avoid overengineering unless you need complex nested types.
- Never assume a configurable is updated in code. Always access live values directly from their source.

---

## Example Configurables Files

### Kotlin Object
@code(/../test-codebase/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/examples/configurables/TestKotlinObject.kt)

### Kotlin Class
@code(/../test-codebase/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/examples/configurables/TestKotlinClass.kt)

### Java Class
@code(/../test-codebase/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/examples/configurables/TestJavaClass.java)

### Showcase OpMode
@code(/../test-codebase/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/examples/configurables/TestOpMode.kt)