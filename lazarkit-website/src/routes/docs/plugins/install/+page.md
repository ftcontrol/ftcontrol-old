# Installation

You can install Panels plugins using **Maven** or by manually adding JAR files, depending on how the plugin is distributed.

## Using Maven

If the plugin is published to a public Maven repository:

1. Open your `TeamCode` module's `build.gradle` file.
2. Add the plugin as a dependency. For example:
```groovy
dependencies {
    implementation 'com.example:panels-plugin:1.0.0'
}
```
3. Sync the project to apply changes.
> Make sure the Maven repository is included in your `repositories` block if it's not a central repository.

---

## Using a JAR File

If the plugin is shared as a `.jar` file:

1. Place the `.jar` file inside `TeamCode/libs` (or `TeamCode/jars` depending on your folder structure).
2. In your `TeamCode` `build.gradle` file, add the following:

```groovy
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
}
```

> Make sure the `libs` folder exists and contains your JAR file before syncing.

---

## Official Plugin JAR Files

You can find official plugin JARs and example setups here:  
🔗 [Panels Plugin Examples on GitHub](https://github.com/lazarcloud/ftcontrol/tree/main/plugins/example/TeamCode/.lib-versions)

---