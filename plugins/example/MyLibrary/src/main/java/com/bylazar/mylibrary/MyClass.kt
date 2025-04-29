package com.bylazar.mylibrary

import com.bylazar.ftcontrol.panels.plugins.ModContext
import com.bylazar.ftcontrol.panels.plugins.Page
import com.bylazar.ftcontrol.panels.plugins.PanelsPlugin

class MyClass : PanelsPlugin() {
    override var id: String = "com.bylazar.myplugin"
    override val name: String = "Lazar's Example Plugin"
    override fun onRegister(context: ModContext) {
        println("DASH: ran internal plugin register")
        createPage("Test Page 1")

        createPage("Test Page 2")
        createPage(
            Page(
            title = "Test HTML",
            getHTML = {
                """
                    <h1>Test Page 3</h1>
                    
                    <p style="color: var(--primary)">Primary colored</p>
                    
                    <button onclick="alert('Hello World!')">Click Me!</button>
                """.trimIndent()
            }
        ))
    }
}