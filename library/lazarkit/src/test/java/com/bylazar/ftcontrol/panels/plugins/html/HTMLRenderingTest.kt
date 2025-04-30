package com.bylazar.ftcontrol.panels.plugins.html

import com.bylazar.ftcontrol.panels.plugins.html.primitives.Text
import com.bylazar.ftcontrol.panels.plugins.html.primitives.p
import org.junit.Assert.assertEquals
import org.junit.Test

class HTMLRenderingTest {
    @Test
    fun testBasic() {
        val testHTML = p { text("Paragraph.") }

        testHTML.childElements.forEach {
            if (it is Text) {
                println("Content: ${it.content}")
            }
        }

        val stringContent = testHTML.html

        assertEquals("""<p>Paragraph.</p>""", stringContent)
    }
}