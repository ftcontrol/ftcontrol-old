# Graph View
The Graph View allows you to plot telemetry data as graphs, making it easier to understand trends, patterns, and anomalies in your robot's performance. It supports multiple data series and time scaling.

Useful for:

- Monitoring on sensor data.
- Tuning control system like PIDs.
- Identifying voltage-related issues.

---

# Video Showcase

<video width="100%" controls>
  <source src="/docs/graph_example.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>

## OpMode used in this example

@code(/../test-codebase/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/examples/telemetry/GraphOpMode.kt)

## The `graph` Function
The `graph` function is used to log data that will later be displayed on the graph view. It works with any numerical type (`Int`, `Long`, `Double`, `Float`).

Example for Logging Variables to the Graph: 

```kotlin
panelsTelemetry.graph(
    "motorPower",
    sensorReading
)
```
---

## Using the Graph View Interface 

<img src="/docs/graph.png"/>

### Selecting Data Series  
- On the left side of the Graph View, you will see a list of available telemetry keys.
- Click on a key to toggle its visibility on the graph.
- Multiple keys can be selected to compare different data series simultaneously.

### Time Scaling  
- At the top of the graph, there is a slider that controls the time window displayed.
- Adjust the slider to zoom in or out on the timeline.
         
     