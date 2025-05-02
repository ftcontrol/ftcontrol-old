package com.bylazar.ftcontrol.panels.plugins.html.primitives

import com.bylazar.ftcontrol.panels.plugins.html.HTMLBuilder
import com.bylazar.ftcontrol.panels.plugins.html.HTMLElement
import com.bylazar.ftcontrol.panels.plugins.html.HTMLTags
import com.bylazar.ftcontrol.panels.plugins.html.HtmlDsl

@HtmlDsl
open class P(
    id: String = "",
    classes: List<String> = emptyList(),
    styles: String = "",
    vararg children: HTMLElement
) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.P
}

inline fun p(
    id: String = "",
    classes: List<String> = emptyList(),
    styles: String = "",
    block: HTMLBuilder.() -> Unit = { }
) = P(id, classes, styles, *HTMLBuilder().apply(block).children.toTypedArray())