# Telemetry

The Telemetry  feature in FTControl provides real-time data logging and visualization capabilities, allowing teams to monitor robot performance directly from their code. By integrating telemetry into OpModes, teams can output key metrics such as sensor values, motor outputs, or custom debug messages, which are then displayed on the Driver Station or within the Panels interface.

---

## Example OpMode File: `TestOpMode.kt`
Below is an example of how telemetry can be implemented in an OpMode: 
@code(/../test-codebase/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/examples/telemetry/TestOpMode.kt)

### The `debug` Function
The `debug` function is used to log data that will later be displayed either on the dashboard or transmitted to the Driver Station. It supports multiple types of inputs, making it versatile for various use cases.
Example for logging multiple text lines:
```kotlin
panelsTelemetry.debug(
    "This is line 1",
    "This is line 2",
    "Sensor value: ${sensorReading}"
)
```

It also supports graphical objects that are covered in the next chapter.

### The `update` Function 
The `update` function is responsible for transmitting logged data to the appropriate display systems. It provides flexibility in where the telemetry data is sent, enabling updates to both the dashboard and the Driver Station.
1. Update Only the Dashboard: 
    If you want to send telemetry data exclusively to the Panels dashboard, you can call the update() method without additional parameters:
    ```kotlin
    panelsTelemetry.update()
    ```
2. Update Both the Dashboard and Driver Station : 
    To simultaneously update the FTC SDK's built-in telemetry system (displayed on the Driver Station) and the Panels dashboard, use the update(telemetry) method:
    ```kotlin
    panelsTelemetry.update(telemetry)
    ```