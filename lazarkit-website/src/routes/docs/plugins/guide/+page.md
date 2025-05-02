# Plugin Development Guide

Panels supports custom plugins written in Kotlin to extend its functionality. Here's a guide to help you understand how plugins are structured, how they work, and how to get started building your own.

> You can find full working examples in the [ftcontrol/plugins/example](https://github.com/lazarcloud/ftcontrol/tree/main/plugins/example) folder.

---

## Defining a Plugin

To create a plugin, you define a **config class** and a **plugin class**:

```kotlin
open class MyConfig : BasePluginConfig() {
    open var test = "test"
}

class MyClass : PanelsPlugin<MyConfig>(MyConfig()) {
    // Plugin logic here
}
```

### About `open` and Configs

- Plugins require a configuration class that extends `BasePluginConfig`.
- `open` variables can be overridden later by users (i.e., teams) who use your plugin.
- This allows plugins to be **customizable per team** without changing the plugin's code.

---

##  Team-Specific Plugin Configurations

Teams using your plugin can customize it by overriding the config:

```kotlin
class MyConfig : MyConfig() {
    override var isDev = true
    override var test = "custom value"
}
```

> `isDev` is a built-in field in `BasePluginConfig`, useful for toggling development-only features.

---

## Global Variables

Plugins can expose **reactive global variables**, which can be displayed dynamically in the UI:

```kotlin
override val globalVariables = mutableMapOf<String, () -> Any>(
    "test" to { 6 },
    "timestamp" to { System.currentTimeMillis() }
)
```

- Use `dynamic("key")` in HTML templates to bind these values.

---

## Plugin Actions

You can define actions (e.g., for buttons):

```kotlin
override val actions = mutableMapOf<String, () -> Unit>(
    "test" to { println("DASH: TEST ACTION") }
)
```

- Actions are triggered from UI elements like `button(action = "test") { text("Run Action") }`.

---

## Plugin Identity

Every plugin must define a unique ID and a name:

```kotlin
override var id: String = "com.bylazar.myplugin"
override val name: String = "Lazar's Example Plugin"
```

- The `id` should be unique to avoid conflicts.

---

## Plugin Lifecycle Hooks

Plugins can respond to lifecycle events:

```kotlin
override fun onEnable() {
    // Called when Panels is enabled
}

override fun onDisable() {
    // Called when Panels is disabled
}

override fun onAttachEventLoop(eventLoop: FtcEventLoop) {
    // Called when FTC SDK's event loop is available
}

override fun onRegister(context: ModContext) {
    // Called once during initialization
}
```

> `ModContext` is currently empty but planned for future extensibility.

---

## Creating Pages

You can create custom UI pages using raw HTML or Kotlin DSL:

### HTML String Example:

```kotlin
createPage(
    Page(
        id = "3",
        title = "Test HTML",
        html = text(
            //language=HTML
            """
                <h1>Test Page</h1>
                <p style="color: var(--primary)">Primary colored</p>
                <button onclick="alert('Hello World!')">Click Me!</button>
            """.trimIndent()
        )
    )
)
```

### HTML DSL Example:

```kotlin
createPage(
    Page(
        id = "4",
        title = "Test HTML Builders",
        html = div {
            p(styles = "color:red;") { dynamic("timestamp") }
            p(styles = "color:blue;") { dynamic("timestamp2") }
            h1 {
                text("Heading1")
                text("Heading2")
                dynamic("test")
            }
            button(action = "test") {
                text("Run Action")
            }
            button(styles = "all: unset; cursor: pointer;") {
                text(iconSVG) // variable with svg icon
            }
        }
    )
)
```

> Each page must have a **unique `id`**.

---

## Special Note on `<script>` Tags

You can add JavaScript to your pages using `text()`:

```kotlin
text("""
    <script>
        document.getElementById("dynamicIFrame").src = "http://" + window.location.hostname + ":5801";
    </script>
""".trimIndent())
```

> ⚠️ Scripts are treated as **ES Modules** — they **don’t share state** across `<script>` tags.  
> Also, `document` refers to the **shadow DOM** of the rendered page.

---

# Panels HTML Engine Features

The HTML DSL includes:

- `button(action = "...") { ... }` — clickable buttons
- `dynamic("key")` — reactive content binding
- `text("...")` — plain text or HTML content
- `div { ... }` — container elements
- `p`, `h1` to `h5`, `span`, `img`, `iframe`
- `widgetHeader()` — renders the widget-style panel header
- `empty { ... }` — for grouping multiple HTML elements

---

## Styling and Theming

Each page supports CSS variables like `--primary`, `--background`, etc., allowing consistent theming across plugins.
You can also add custom CSS with `text("<style></style>")`.

### Full styling reference:

@code(/../library/lazarkit/src/main/web/src/lib/ui/styles.ts)

---