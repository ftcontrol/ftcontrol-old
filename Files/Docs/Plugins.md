Generic plugin interface with life cycle hooks
```kotlin
interface DashboardPlugin {
    fun getName(): String
    fun onRegister(dashboard: Dashboard)
}
```

### Load Plugin `.jar` files Dynamically

When your dashboard starts, **look into an `/addons/` folder** for `.jar` files, and **load them dynamically**.

You can use **Java's `URLClassLoader`** for this:
```kotlin
val pluginDir = File("addons")
val pluginFiles = pluginDir.listFiles { file -> file.extension == "jar" } ?: arrayOf()

for (pluginFile in pluginFiles) {
    val classLoader = URLClassLoader(arrayOf(pluginFile.toURI().toURL()), this.javaClass.classLoader)
    val serviceLoader = ServiceLoader.load(DashboardPlugin::class.java, classLoader)

    for (plugin in serviceLoader) {
        plugin.onRegister(dashboardInstance)
        println("Loaded plugin: ${plugin.getName()}")
    }
}

```

### 3. Find the plugin class

**Inside each plugin jar, require a `plugin.json` file** or a special marker class. Example:

**`plugin.json` inside jar:**
```json
{
  "mainClass": "com.example.MyAwesomePlugin"
}
```

```kotlin
val pluginJsonStream = loader.getResourceAsStream("plugin.json")
val pluginJson = pluginJsonStream?.bufferedReader()?.use { it.readText() }
val mainClassName = JSONObject(pluginJson).getString("mainClass")

```

4. Load and instantiate the plugin

```kotlin
val pluginClass = loader.loadClass(mainClassName)
val pluginInstance = pluginClass.getDeclaredConstructor().newInstance() as DashboardPlugin
`pluginInstance.onRegister(context)`
```

### Teams build their plugins as `.jar` files

You provide an **SDK** (just a `.jar` with your `DashboardPlugin` interface and any helper classes they might need).

Teams would:

- Create a small Kotlin project
- Implement the `DashboardPlugin`
- Build it as a `.jar`
- Drop it into `/addons/`

### Hot reloading
### HTML Support
Use server side rendering, dynamic / static content
Use iframes
```html
<iframe src="http://localhost:8080/plugins/myplugin/index.html" />

<script>
  export let iframeUrl = "";
</script>

<iframe src={iframeUrl} style="width: 100%; height: 100%; border: none;"></iframe>

```
Use a generic selector with plugin id for all css
define js callbacks
Either dynamic or static

### Super Quick Code Idea (Svelte Widget Renderer)
```html
<script>
  export let htmlContent = "";
</script>

<div>
  {@html htmlContent}
</div>
```

### Communication

1. Event bus

Plugins can **broadcast events** → Other plugins can **listen**.

```kotlin
interface EventBus {
    fun emit(event: String, data: Any?)
    fun on(event: String, handler: (Any?) -> Unit)
}

// Plugin A
eventBus.emit("robot_connected", RobotInfo(...))

// Plugin B
eventBus.on("robot_connected") { data ->
    val robotInfo = data as RobotInfo
    println("Robot connected with name: ${robotInfo.name}")
}


class SimpleEventBus : EventBus {
    private val listeners = mutableMapOf<String, MutableList<(Any?) -> Unit>>()

    override fun emit(event: String, data: Any?) {
        listeners[event]?.forEach { handler -> handler(data) }
    }

    override fun on(event: String, handler: (Any?) -> Unit) {
        listeners.computeIfAbsent(event) { mutableListOf() }.add(handler)
    }
}

```

2. Services

Plugins can **offer services** to others.
```kotlin
interface ServiceRegistry {
    fun <T> register(serviceClass: KClass<T>, service: T)
    fun <T> get(serviceClass: KClass<T>): T?
}
// Plugin A
serviceRegistry.register(CameraFeedService::class, MyCameraService())

// Plugin B
val cameraService = serviceRegistry.get(CameraFeedService::class)
cameraService?.startFeed()

```