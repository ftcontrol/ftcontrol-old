package com.bylazar.ftcontrol.panels.plugins.html.primitives

import com.bylazar.ftcontrol.panels.plugins.html.HTMLElement
import com.bylazar.ftcontrol.panels.plugins.html.HTMLTags
import com.bylazar.ftcontrol.panels.plugins.html.HtmlDsl

@HtmlDsl
open class DynamicValue(
    override var id: String,
) : HTMLElement() {
    override val tag: HTMLTags = HTMLTags.TEXT

    override val html: String
        get() = "<DYNAMIC>${id}</DYNAMIC>"
}

fun dynamic(
    id: String,
) = DynamicValue(id)