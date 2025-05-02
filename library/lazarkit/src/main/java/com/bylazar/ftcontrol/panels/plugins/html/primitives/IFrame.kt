package com.bylazar.ftcontrol.panels.plugins.html.primitives

import com.bylazar.ftcontrol.panels.plugins.html.HTMLBuilder
import com.bylazar.ftcontrol.panels.plugins.html.HTMLElement
import com.bylazar.ftcontrol.panels.plugins.html.HTMLTags
import com.bylazar.ftcontrol.panels.plugins.html.HtmlDsl
import java.util.Locale

@HtmlDsl
open class IFrame(
    id: String = "",
    classes: List<String> = emptyList(),
    src: String,
    title: String,
    styles: String = "",
    vararg children: HTMLElement
) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.IFRAME

    override val extraContent: String = "src=\"$src\" title=\"$title\""
}

fun iframe(
    id: String = "",
    classes: List<String> = emptyList(),
    src: String,
    title: String,
    styles: String = "",
) = IFrame(id, classes, src, title, styles)
