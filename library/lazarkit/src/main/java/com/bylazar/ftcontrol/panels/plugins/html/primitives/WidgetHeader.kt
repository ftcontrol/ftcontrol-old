package com.bylazar.ftcontrol.panels.plugins.html.primitives

import com.bylazar.ftcontrol.panels.plugins.html.HtmlDsl

@HtmlDsl
open class WidgetHeader(
    title: String
) : Div(classes = listOf("widget-header"), children = arrayOf(P(children = arrayOf(Text(title)))))

fun widgetHeader(
    title: String
) = WidgetHeader(title)
