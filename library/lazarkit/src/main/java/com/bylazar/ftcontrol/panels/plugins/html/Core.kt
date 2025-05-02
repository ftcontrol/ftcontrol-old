package com.bylazar.ftcontrol.panels.plugins.html

import java.util.Locale


enum class HTMLTags {
    DIV,
    P,
    BUTTON,
    H1,
    H2,
    H3,
    H4,
    H5,
    SPAN,
    TEXT,
    CUSTOM,
    IFRAME,
    EMPTY,
    IMG
}

@HtmlDsl
abstract class HTMLElement(
    open val id: String = "",
    val classNames: List<String> = emptyList(),
    val styles: String = "",
    vararg children: HTMLElement
) {
    abstract val tag: HTMLTags
    open val extraContent: String = ""

    val childElements: MutableList<HTMLElement> = children.toMutableList()

    open val html: String
        get() {
            val sb = StringBuilder()
            sb.append("<${tag.name.lowercase(Locale.getDefault())}")

            if (id.isNotEmpty()) sb.append(" id=\"$id\"")
            if (classNames.isNotEmpty()) sb.append(" class=\"${classNames.joinToString(" ")}\"")
            if (styles.isNotEmpty()) sb.append(" style=\"$styles\"")
            if (extraContent.isNotEmpty()) sb.append(" $extraContent")

            sb.append(">")

            for (child in childElements) {
                sb.append(child.html)
            }

            sb.append("</${tag.name.lowercase(Locale.getDefault())}>")
            return sb.toString()
        }
}