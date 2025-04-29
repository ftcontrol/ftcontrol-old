package com.bylazar.ftcontrol.panels.plugins.html

import java.util.Locale

enum class HTMLTags{
    DIV,
    P,
    BUTTON,
    H1,
    H2,
    H3,
    H4,
    H5,
    SPAN,
}

abstract class HTMLElement(
    val id: String = "",
    val classNames: MutableList<String> = mutableListOf(),
    val styles: String = "",
    vararg val children: HTMLElement
){
    abstract val tag: HTMLTags
    abstract val extraContent: String

    val html: String
        get() = """
            <${tag.name.toString().lowercase()}
            ${if (id.isNotEmpty()) "id="$id"" else ""}
            ${if (classNames.isNotEmpty()) "class="${classNames.joinToString(" ")}"" else ""}
            ${if (styles.isNotEmpty()) "style="$styles"" else ""}
            ${if (extraContent.isNotEmpty()) extraContent else ""}
            >
            ${children.joinToString("") { it.html }}
            </${tag.name.toString().lowercase()}>
        """.trimIndent()
}