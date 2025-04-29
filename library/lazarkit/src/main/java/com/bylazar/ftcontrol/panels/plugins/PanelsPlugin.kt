package com.bylazar.ftcontrol.panels.plugins

import kotlinx.serialization.Serializable

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
}

class ModContext(
) {
}

@Serializable
class Page(
    var title: String,
)