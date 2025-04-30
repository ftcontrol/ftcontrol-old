package com.bylazar.ftcontrol.panels.plugins.html.primitives

import com.bylazar.ftcontrol.panels.plugins.html.HTMLBuilder
import com.bylazar.ftcontrol.panels.plugins.html.HTMLElement
import com.bylazar.ftcontrol.panels.plugins.html.HTMLTags
import com.bylazar.ftcontrol.panels.plugins.html.HtmlDsl

@HtmlDsl
open class Button(
    id: String = "",
    classes: List<String> = emptyList(),
    styles: String = "",
    val action: String = "",
    vararg children: HTMLElement
) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.BUTTON
    override val extraContent: String
        get() {
            if(action == "") return ""
            return "class='action' data-action='$action'"
        }
}

inline fun button(
    id: String = "",
    classes: List<String> = emptyList(),
    styles: String = "",
    action: String = "",
    block: HTMLBuilder.() -> Unit = { }
) = Button(id, classes, styles, action, *HTMLBuilder().apply(block).children.toTypedArray())