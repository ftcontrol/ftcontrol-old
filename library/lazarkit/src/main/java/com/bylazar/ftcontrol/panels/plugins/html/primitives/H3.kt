package com.bylazar.ftcontrol.panels.plugins.html.primitives

import com.bylazar.ftcontrol.panels.plugins.html.HTMLBuilder
import com.bylazar.ftcontrol.panels.plugins.html.HTMLElement
import com.bylazar.ftcontrol.panels.plugins.html.HTMLTags
import com.bylazar.ftcontrol.panels.plugins.html.HtmlDsl

@HtmlDsl
open class H3(
    id: String = "",
    classes: List<String> = emptyList(),
    styles: String = "",
    vararg children: HTMLElement
) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.H3
}

inline fun h3(
    id: String = "",
    classes: List<String> = emptyList(),
    styles: String = "",
    block: HTMLBuilder.() -> Unit
) = H3(id, classes, styles, *HTMLBuilder().apply(block).children.toTypedArray())