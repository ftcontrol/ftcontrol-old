package com.bylazar.ftcontrol.panels.plugins.html

@DslMarker
annotation class HtmlDsl

inline fun HTMLElement.div(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: Div.() -> Unit) {
    val div = Div(id, classes, styles).apply(block)
    addChild(div)
}
inline fun div(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: Div.() -> Unit): Div {
    val div = Div(id, classes, styles).apply(block)
    return div
}

inline fun HTMLElement.p(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: P.() -> Unit) {
    val p = P(id, classes, styles).apply(block)
    addChild(p)
}
inline fun p(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: P.() -> Unit): P {
    val p = P(id, classes, styles).apply(block)
    return p
}

inline fun HTMLElement.button(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: Button.() -> Unit) {
    val button = Button(id, classes, styles).apply(block)
    addChild(button)
}
inline fun button(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: Button.() -> Unit): Button {
    val button = Button(id, classes, styles).apply(block)
    return button
}

inline fun HTMLElement.h1(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: H1.() -> Unit) {
    val h1 = H1(id, classes, styles).apply(block)
    addChild(h1)
}
inline fun h1(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: H1.() -> Unit): H1 {
    val h1 = H1(id, classes, styles).apply(block)
    return h1
}

inline fun HTMLElement.h2(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: H2.() -> Unit) {
    val h2 = H2(id, classes, styles).apply(block)
    addChild(h2)
}
inline fun h2(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: H2.() -> Unit): H2 {
    val h2 = H2(id, classes, styles).apply(block)
    return h2
}

inline fun HTMLElement.h3(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: H3.() -> Unit) {
    val h3 = H3(id, classes, styles).apply(block)
    addChild(h3)
}
inline fun h3(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: H3.() -> Unit): H3 {
    val h3 = H3(id, classes, styles).apply(block)
    return h3
}

inline fun HTMLElement.h4(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: H4.() -> Unit) {
    val h4 = H4(id, classes, styles).apply(block)
    addChild(h4)
}
inline fun h4(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: H4.() -> Unit): H4 {
    val h4 = H4(id, classes, styles).apply(block)
    return h4
}

inline fun HTMLElement.h5(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: H5.() -> Unit) {
    val h5 = H5(id, classes, styles).apply(block)
    addChild(h5)
}
inline fun h5(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: H5.() -> Unit): H5 {
    val h5 = H5(id, classes, styles).apply(block)
    return h5
}

inline fun HTMLElement.span(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: Span.() -> Unit) {
    val span = Span(id, classes, styles).apply(block)
    addChild(span)
}
inline fun span(id: String = "", classes: List<String> = emptyList(), styles: String = "", block: Span.() -> Unit): Span {
    val span = Span(id, classes, styles).apply(block)
    return span
}

fun HTMLElement.text(content: String) {
    this.textContent = content
}