package com.bylazar.ftcontrol.panels.plugins

import com.bylazar.ftcontrol.panels.plugins.html.HTMLElement
import com.bylazar.ftcontrol.panels.plugins.html.primitives.Text
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class PluginJson(
    val globalVariables: Map<String, String>,
    val id: String,
    val name: String,
    val pages: List<PageJson>
)

abstract class PanelsPlugin {
    abstract val globalVariables: Map<String, () -> Any>

    internal var pages = mutableListOf<Page>()

    abstract var id: String
    abstract val name: String
    abstract fun onRegister(context: ModContext)

    fun createPage(title: String) {
        pages.add(
            Page(
                title = title
            )
        )
    }

    fun createPage(page: Page) {
        pages.add(page)
    }

    val cachedPages: List<PageJson> by lazy {
        pages.map { it.toJson }
    }

    val toJson: PluginJson
        get() = PluginJson(
            globalVariables = globalVariables.mapValues { it.value().toString() },
            id = id,
            name = name,
            pages = cachedPages
        )
}

class ModContext(
) {
}

class Page(
    var title: String,
    var html: HTMLElement = Text(""),
    val id: String = UUID.randomUUID().toString(),
) {
    val toJson: PageJson
        get() = PageJson(
            title = title,
            id = id,
            html = html.html,
        )
}

@Serializable
class PageJson(
    var title: String,
    val id: String,
    var html: String,
)