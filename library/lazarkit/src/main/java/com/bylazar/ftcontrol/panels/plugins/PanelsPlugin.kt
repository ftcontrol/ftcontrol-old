package com.bylazar.ftcontrol.panels.plugins

import com.bylazar.ftcontrol.panels.plugins.html.HTMLElement
import com.bylazar.ftcontrol.panels.plugins.html.primitives.Text
import com.qualcomm.ftccommon.FtcEventLoop
import kotlinx.serialization.Serializable

@Serializable
class PluginJson(
    val globalVariables: Map<String, String>,
    val id: String,
    val name: String,
    val pages: List<PageJson>
)

open class BasePluginConfig {
    open var isDev: Boolean = false
    open var isEnabled: Boolean = true
}

abstract class PanelsPlugin<T : BasePluginConfig>(baseConfig: T) {
    val isDev: Boolean
        get() = config.isDev
    val isEnabled: Boolean
        get() = config.isEnabled

    var config = baseConfig

    abstract val globalVariables: MutableMap<String, () -> Any>
    abstract val actions: MutableMap<String, () -> Unit>
    var pages = mutableListOf<Page>()

    abstract var id: String
    abstract val name: String
    abstract fun onRegister(context: ModContext)
    abstract fun onEnable()
    abstract fun onDisable()
    abstract fun onAttachEventLoop(eventLoop: FtcEventLoop)

    fun handleConfig(foundClasses: List<ClassFinder.ClassEntry>) {
        foundClasses.forEach {
            val clazz = Class.forName(it.className)
            val isConfig = config::class.java.isAssignableFrom(clazz)
            val pluginPackage = javaClass.`package`?.name ?: return@forEach
            val clazzPackage = clazz.`package`?.name ?: return@forEach

            if (pluginPackage == clazzPackage || clazzPackage.startsWith("com.bylazar.ftcontrol") || !isConfig) {
                return@forEach
            }

            val newConfig = try {
                clazz.getDeclaredConstructor().newInstance()
            } catch (e: Exception) {
                e.printStackTrace()
                return@forEach
            }

            @Suppress("UNCHECKED_CAST")
            config = newConfig as T

            println("DASH: Found config class: $clazz / $isConfig / $pluginPackage / $clazzPackage")

            //TODO: handle multiple configs
        }
    }

    fun createPage(id: String, title: String) {
        createPage(
            Page(
                id = id,
                title = title
            )
        )
    }

    fun createPage(page: Page) {
        //TODO: handle duplicate ids
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
    val id: String
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