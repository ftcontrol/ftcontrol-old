package com.bylazar.ftcontrol.panels.plugins

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class PluginJson(
    val id: String,
    val name: String,
    val pages: List<PageJson>
)

abstract class PanelsPlugin {
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

    val toJson: PluginJson
        get() = PluginJson(
            id = id,
            name = name,
            pages = pages.map { it.toJson }
        )
}

class ModContext(
) {
}

class Page(
    var title: String,
    var getHTML: () -> String = { "" },
    val id: String = UUID.randomUUID().toString(),
) {
    val toJson: PageJson
        get() = PageJson(
            title = title,
            id = id,
            html = getHTML()
        )
}

@Serializable
class PageJson(
    var title: String,
    val id: String,
    var html: String
)