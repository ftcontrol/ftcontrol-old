# TPair

TPair  is a utility class in the ftcontrol library for mapping configurable key-value pairs  between two types, commonly used for robotic mechanism position control (e.g., mapping states like OPENED/CLOSED to servo positions). 

## Key Features 
1. Maps semantic states  (e.g., Enum) to numeric/digital values  (e.g., Double, Boolean)
2. Supports runtime configuration  via the ftcontrol UI
3. Provides a default fallback value  for unmatched keys

Use `@GenericValue(KeyType::class, ValueType::class)` to preserve type information at runtime (required due to Kotlin type erasure):
Use `val currentPosition = poses.get()` to access current value.

---

@code(/../test-codebase/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/examples/configurables/ClawConfig.kt)

<img src="/docs/tpair.png">