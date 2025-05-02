package com.bylazar.ftcontrol.panels.plugins.html.primitives

import com.bylazar.ftcontrol.panels.plugins.html.HTMLBuilder
import com.bylazar.ftcontrol.panels.plugins.html.HTMLElement
import com.bylazar.ftcontrol.panels.plugins.html.HTMLTags
import com.bylazar.ftcontrol.panels.plugins.html.HtmlDsl

@HtmlDsl
open class Empty(
    vararg children: HTMLElement
) :
    HTMLElement(children = children) {
    override val tag: HTMLTags = HTMLTags.EMPTY

    override val html: String
        get() {
            val sb = StringBuilder()
            childElements.forEach { sb.append(it.html) }
            return sb.toString()
        }
}

inline fun empty(
    block: HTMLBuilder.() -> Unit = { }
) = Empty(children = HTMLBuilder().apply(block).children.toTypedArray())
