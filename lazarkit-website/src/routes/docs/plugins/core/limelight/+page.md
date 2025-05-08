# Limelight Proxy

The Limelight Proxy plugin allows full integration with the Limelight 3A camera system inside the Panels dashboard environment.

## Installing the plugin

Get the jar from [Limelight Plugin JAR](https://github.com/lazarcloud/ftcontrol/tree/main/plugins/example/TeamCode/.lib-versions) and use the [Installation Guide](/docs/plugins/install) to add it to your TeamCode project.

> Add `implementation 'com.squareup.okhttp3:okhttp:4.10.0'` to your TeamCode dependencies. This will not be required in a future update.

Using the `Limelight` tab inside Plugin's Settings or the Widgets you can preview the original Limelight3A Dashboard. You can also use port :5801 with the IP of your Robot Controller app.

## Widgets
- LL Dash: Core dashboard for interacting with a Limelight camera's settings, modes, and outputs.
- LL Feed: Live video feed directly from a Limelight, integrated into your dashboard for easy vision monitoring.

<img src="/docs/limelight.png"/>