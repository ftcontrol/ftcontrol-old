# Customization

Panels offers a customizable control grid for easier and more flexible management of your robot systems and tools.
You can arrange the layout, choose which widgets to display, and tailor the dashboard to fit your team's workflow.

## Terminology
- Widget: The core component of Panels
- Widget Group: A group of widgets with tabs
- Preset:  A grid layout of widget groups

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

> You can open a context menu by right clicking on an element.

### Tabs
Tabs can be moved around by dragging.

<video width="100%" controls>
  <source src="/docs/custom_tabs.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>

### Moving and Resizing Groups
You can manage widget groups by resizing them using the right bottom icon and moving them from the top bar.

<video width="100%" controls>
  <source src="/docs/custom_groups.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>

### Managing tabs
You can create, delete, change tabs types from the context menu.

<video width="100%" controls>
  <source src="/docs/custom_tabs_new.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>

### Managing Presets
Using the sidebar context menu you can manage presets.

<video width="100%" controls>
  <source src="/docs/custom_presets.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>


## Gotchas

### Saving
All changes are saved automatically.

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