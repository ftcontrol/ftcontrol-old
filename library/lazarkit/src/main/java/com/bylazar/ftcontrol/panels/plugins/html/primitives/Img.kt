package com.bylazar.ftcontrol.panels.plugins.html.primitives

import com.bylazar.ftcontrol.panels.plugins.html.HTMLElement
import com.bylazar.ftcontrol.panels.plugins.html.HTMLTags
import com.bylazar.ftcontrol.panels.plugins.html.HtmlDsl

@HtmlDsl
open class Img(
    id: String = "",
    classes: List<String> = emptyList(),
    src: String,
    alt: String,
    styles: String = "",
    vararg children: HTMLElement
) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.IMG

    override val extraContent: String = "src=\"$src\" alt=\"$alt\""
}

fun img(
    id: String = "",
    classes: List<String> = emptyList(),
    src: String,
    alt: String = "",
    styles: String = "",
) = Img(id, classes, src, alt, styles)
