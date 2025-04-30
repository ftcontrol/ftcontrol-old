package com.bylazar.ftcontrol.panels.plugins.html.primitives

import com.bylazar.ftcontrol.panels.plugins.html.HTMLElement
import com.bylazar.ftcontrol.panels.plugins.html.HTMLTags
import com.bylazar.ftcontrol.panels.plugins.html.HtmlDsl

@HtmlDsl
open class Text(
    var content: String
) : HTMLElement() {
    override val tag: HTMLTags = HTMLTags.TEXT

    override val html: String
        get() = content
}

fun text(
    content: String
): Text {
    return Text(content)
}