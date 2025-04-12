# 🌟 Field View
It provides a dynamic and interactive visualization of the game field, allowing teams to monitor their robot's position, orientation, and interactions with game elements in real-time. This feature is particularly useful for debugging autonomous flows, tracking robot movements.

It works live a canvas, so it can be used for any drawing application.

## 🎨 Supported Objects
1. Line
2. Circle
3. Rectangle

more will be added soon

## ⚠️ Gotchas
1. Objects are drawn in the order they are sent to the `debug` function, and their layering (z-index) is automatically managed by the system.
2. The input order is flexible, you can provide Drawable objects and text lines in any order within the debug function.
3. Every Drawable object requires a `look` to define its visual appearance. The look specifies styling attributes such as fillColor, outlineColor, outlineWidth.

## 📋 Example OpMode File: `TestOpMode.kt`
Below is an example of how field telemetry can be implemented in an OpMode:

@code(/../test-codebase/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/examples/field/TestOpMode.kt)