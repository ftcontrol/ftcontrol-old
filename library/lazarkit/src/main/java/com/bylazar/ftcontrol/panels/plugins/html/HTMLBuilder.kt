package com.bylazar.ftcontrol.panels.plugins.html

import com.bylazar.ftcontrol.panels.plugins.html.primitives.button as buttonHelper
import com.bylazar.ftcontrol.panels.plugins.html.primitives.div as divHelper
import com.bylazar.ftcontrol.panels.plugins.html.primitives.dynamic as dynamicHelper
import com.bylazar.ftcontrol.panels.plugins.html.primitives.h1 as h1Helper
import com.bylazar.ftcontrol.panels.plugins.html.primitives.p as pHelper
import com.bylazar.ftcontrol.panels.plugins.html.primitives.span as spanHelper
import com.bylazar.ftcontrol.panels.plugins.html.primitives.text as textHelper

class HTMLBuilder(
    val children: MutableList<HTMLElement> = mutableListOf()
) {
    private fun add(htmlElement: HTMLElement) {
        children.add(htmlElement)
    }

    fun div(
        id: String = "",
        classes: List<String> = emptyList(),
        styles: String = "",
        block: HTMLBuilder.() -> Unit = { }
    ) = divHelper(id, classes, styles, block).also { add(it) }

    fun button(
        id: String = "",
        classes: List<String> = emptyList(),
        styles: String = "",
        action: String = "",
        block: HTMLBuilder.() -> Unit = { }
    ) = buttonHelper(id, classes, styles, action, block).also { add(it) }

    fun h1(
        id: String = "",
        classes: List<String> = emptyList(),
        styles: String = "",
        block: HTMLBuilder.() -> Unit = { }
    ) = h1Helper(id, classes, styles, block).also { add(it) }

    fun p(
        id: String = "",
        classes: List<String> = emptyList(),
        styles: String = "",
        block: HTMLBuilder.() -> Unit = { }
    ) = pHelper(id, classes, styles, block).also { add(it) }

    fun span(
        id: String = "",
        classes: List<String> = emptyList(),
        styles: String = "",
        block: HTMLBuilder.() -> Unit = { }
    ) = spanHelper(id, classes, styles, block).also { add(it) }

    fun text(content: String) = textHelper(content).also { add(it) }

    fun dynamic(id: String) = dynamicHelper(id).also { add(it) }
}