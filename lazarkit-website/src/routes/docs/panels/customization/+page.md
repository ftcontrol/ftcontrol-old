# Customization

Panels offers a customizable control grid for easier and more flexible management of your robot systems and tools.
You can arrange the layout, choose which widgets to display, and tailor the dashboard to fit your team's workflow.

## Supported Widgets

Here’s a quick overview of the currently supported widgets:

- OpMode Control: Manage and select active OpModes, start and stop them easily from the dashboard.
- Gamepad: Visualize the state of connected gamepads: button presses, joystick positions, triggers, and more.
- Field: A top-down representation of the competition field. Useful for live tracking of robot position and paths.
- Telemetry: View custom telemetry data sent from the robot code in real time.
- Configurables: Adjust robot parameters on the fly without needing to redeploy your code. Useful for tuning constants.
- Graph: Plot live data (such as velocities, PID values, sensor readings) over time with configurable graphing options.
- Capture: Take snapshots of telemetry or field data during operation for later review and debugging.
- Test: An empty widget 

> Users can create custom widgets using plugins.

## Controls
Each widget has a control bar at the top with several useful buttons.

<img src="/docs/customization_controls.png"/>
<img src="/docs/customization_controls2.png"/>

| # | Control | Description |
|:-|:---|:---|
| 1 | **Move** | Drag the widget to a new position on the dashboard. |
| 2 | **Expand Horizontally** | Make the widget wider (horizontally). |
| 3 | **Shrink Horizontally** | Make the widget narrower (horizontally). |
| 4 | **Expand Vertically** | Make the widget taller (vertically). |
| 5 | **Shrink Vertically** | Make the widget shorter (vertically). |
| 6 | **Widget Type Label** | Shows the type of the widget (example: "OpMode Control"). |
| 7 | **Delete Widget** | Remove the widget from the dashboard. |
| 8 | **Create Widget** | Create a new widget in that slot. |
| 9 | **Manage Presets** | Create, Delete, Rename Presets. |
| 10 | **Manage Edit Mode** | Enter Edit Mode and save changes. |

## Gotchas

### Unknown Widget Type Error
Occasionally, when internal widget definitions are updated (for example, after a software update), you might encounter an Unknown Widget Type Error.
This usually happens because older saved presets no longer match the new widget definitions.

How to fix:
1. Go to Settings.
2. Find the Presets section.
3. Reset all presets. (This will delete all your current layouts.)
<img src="/docs/customization_ids.png"/>

Alternative fix: delete the `presets` cookie.
<img src="/docs/cookies.png"/>