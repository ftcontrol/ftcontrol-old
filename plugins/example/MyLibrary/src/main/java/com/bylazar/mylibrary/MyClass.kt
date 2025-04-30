package com.bylazar.mylibrary

import com.bylazar.ftcontrol.panels.plugins.ModContext
import com.bylazar.ftcontrol.panels.plugins.Page
import com.bylazar.ftcontrol.panels.plugins.PanelsPlugin
import com.bylazar.ftcontrol.panels.plugins.html.primitives.div
import com.bylazar.ftcontrol.panels.plugins.html.primitives.text

class MyClass : PanelsPlugin() {
    //    TODO: error handling
    override val globalVariables = mapOf<String, () -> Any>(
        "test" to { 6 },
        "timestamp" to { System.currentTimeMillis() }
    )

    override val actions = mapOf<String, () -> Unit>(
        "test" to { println("DASH: TEST ACTION") }
    )

    override var id: String = "com.bylazar.myplugin"
    override val name: String = "Lazar's Example Plugin"
    override fun onRegister(context: ModContext) {
        println("DASH: ran internal plugin register")
        createPage("Test Page 1")

        createPage("Test Page 2")
        createPage(
            Page(
                title = "Test HTML",
                html = text(
                    //language=HTML
                    """
                    <h1>Test Page 3</h1>
                    
                    <p style="color: var(--primary)">Primary colored</p>
                    
                    <button onclick="alert('Hello World!')">Click Me!</button>
                    """.trimIndent()
                )
            )
        )
        createPage(
            Page(
                title = "Test HTML Builders",
                html = div {
                    p(styles = "color:red;") { dynamic("timestamp") }
                    p(styles = "color:blue;") { dynamic("timestamp2") }
                    h1 {
                        text("Heading")
                        text("Heading2")
                        dynamic("test")
                    }
                    button(action = "test") {
                        text("Run SSA")
                    }
                }
            ))
    }
}