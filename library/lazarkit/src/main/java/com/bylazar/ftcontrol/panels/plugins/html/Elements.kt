package com.bylazar.ftcontrol.panels.plugins.html

@HtmlDsl
open class Div(id: String = "", classes: List<String> = emptyList(), styles: String = "", vararg children: HTMLElement) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.DIV
}

@HtmlDsl
open class P(id: String = "", classes: List<String> = emptyList(), styles: String = "", vararg children: HTMLElement) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.P
}

@HtmlDsl
open class Button(id: String = "", classes: List<String> = emptyList(), styles: String = "", vararg children: HTMLElement) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.BUTTON
}

@HtmlDsl
open class H1(id: String = "", classes: List<String> = emptyList(), styles: String = "", vararg children: HTMLElement) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.H1
}

@HtmlDsl
open class H2(id: String = "", classes: List<String> = emptyList(), styles: String = "", vararg children: HTMLElement) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.H2
}

@HtmlDsl
open class H3(id: String = "", classes: List<String> = emptyList(), styles: String = "", vararg children: HTMLElement) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.H3
}

@HtmlDsl
open class H4(id: String = "", classes: List<String> = emptyList(), styles: String = "", vararg children: HTMLElement) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.H4
}

@HtmlDsl
open class H5(id: String = "", classes: List<String> = emptyList(), styles: String = "", vararg children: HTMLElement) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.H5
}

@HtmlDsl
open class Span(id: String = "", classes: List<String> = emptyList(), styles: String = "", vararg children: HTMLElement) :
    HTMLElement(id, classes, styles, *children) {
    override val tag: HTMLTags = HTMLTags.SPAN
}