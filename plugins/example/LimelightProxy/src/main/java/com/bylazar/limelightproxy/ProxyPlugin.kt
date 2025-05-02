package com.bylazar.limelightproxy

import com.bylazar.ftcontrol.panels.plugins.BasePluginConfig
import com.bylazar.ftcontrol.panels.plugins.ModContext
import com.bylazar.ftcontrol.panels.plugins.Page
import com.bylazar.ftcontrol.panels.plugins.PanelsPlugin
import com.bylazar.ftcontrol.panels.plugins.html.primitives.div
import com.bylazar.ftcontrol.panels.plugins.html.primitives.empty
import com.bylazar.limelightproxy.proxies.GenericProxy
import com.bylazar.limelightproxy.proxies.GenericSocketProxy
import com.bylazar.limelightproxy.proxies.GenericStreamingProxy
import com.qualcomm.ftccommon.FtcEventLoop

open class ProxyPluginConfig : BasePluginConfig() {
    open var customString = "hi!"
}

class ProxyPlugin : PanelsPlugin<ProxyPluginConfig>(ProxyPluginConfig()) {
    override val globalVariables: MutableMap<String, () -> Any> = mutableMapOf(
        "isDev" to { isDev },
        "isProxied" to { isProxied },
        "customString" to { config.customString }
    )
    override val actions: MutableMap<String, () -> Unit> = mutableMapOf(
        "toggle" to {
            isProxied = !isProxied
        },
    )
    override var id: String = "com.bylazar.limelightproxy"
    override val name: String = "Limelight Proxy"

    lateinit var limelightProxy: GenericProxy
    lateinit var limelightFeedProxy: GenericStreamingProxy
    lateinit var limelightWebsocketProxy: GenericSocketProxy
    lateinit var limelightAPIProxy: GenericProxy
    lateinit var testServer: TestLimelightServer

    var isProxied = false
        set(value) {
            when (value) {
                true -> {
                    limelightProxy.startServer()
                    limelightFeedProxy.startServer()
                    limelightWebsocketProxy.startProxy()
                    limelightAPIProxy.startServer()
                    if (isDev) limelightProxy.startServer()
                }

                false -> {
                    limelightProxy.stopServer()
                    limelightFeedProxy.stopServer()
                    limelightWebsocketProxy.stopProxy()
                    limelightAPIProxy.stopServer()
                    if (isDev) limelightProxy.stopServer()
                }
            }
            field = value
        }

    override fun onRegister(context: ModContext) {
        println("DASH: isDev $isDev")
        if (isDev) {
            limelightProxy = GenericProxy(5801, 3331, "localhost")
            limelightFeedProxy = GenericStreamingProxy(5800, 3331, "localhost")
            testServer = TestLimelightServer()
            testServer.startServer()

        } else {
            limelightProxy = GenericProxy(5801, 5801, "172.29.0.1")
            limelightFeedProxy = GenericStreamingProxy(5800, 5800, "172.29.0.1")
        }

        limelightWebsocketProxy = GenericSocketProxy(5805, 5805, "172.29.0.1")
        limelightAPIProxy = GenericProxy(5807, 5807, "172.29.0.1")

        isProxied = true

        if(isDev) createPage(
            Page(
                id = "test",
                title = "Limelight Dash",
                html = div {
                    p {
                        text("isProxied: ")
                        dynamic("isProxied")
                    }
                    button(action = "toggle") {
                        text("Toggle")
                    }
                    text("<iframe src=\"http://localhost:5801/\" title=\"Limelight Dashboard\"> </iframe>")
                    p {
                        text("customString: ")
                        dynamic("customString")
                    }
                    p {
                        text("isDev: ")
                        dynamic("isDev")
                    }
                }
            ))

        fun genericScript(path: String): String {
            return """
            <style>
            .isDisabled {
                opacity: 0.5;
              }
            </style>
            <script>
                async function fetchCurrentURL() {
                    const wrapper = document.getElementById('wrapper');
                    try {
                        const url = "http://" + window.location.hostname + "$path";
                        console.log("Fetching URL");
            
                        const response = await fetch(url);
            
                        if (!response.ok) {
                            throw new Error(`HTTP error!`);
                        } 
                        console.log("Fetch successful");
                    } catch (error) {
                        console.error("Failed to fetch URL:", error);
                        wrapper.classList.add('isDisabled');
                    }
                }
                fetchCurrentURL();
            </script>
        """.trimIndent()
        }

        createPage(
            Page(
                id = "fullSizedDash",
                title = "Full Size Dash Page",
                html = empty {
                    text(genericScript(":5801"))
                    text("""
                        <script>
                            document.getElementById("dynamicIFrame").src = "http://" + window.location.hostname + ":5801";
                        </script>
                    """.trimIndent())
                    widgetHeader("Limelight Dashboard")
                    iframe(
                        id = "dynamicIFrame",
                        src = "",
                        title = "Limelight Dashboard",
                        styles = """
                        width: 100%;
                        height: 100%;
                    """.trimIndent()
                    )
                }
            )
        )
        createPage(
            Page(
                id = "fullSizedFeed",
                title = "Full Size Feed Page",
                html = empty {
                    text(genericScript(":5800"))
                    text("""
                        <script>
                            document.getElementById("dynamicImage").src = "http://" + window.location.hostname + ":5800";
                        </script>
                    """.trimIndent())
                    widgetHeader("Limelight Feed")
                    img(
                        id="dynamicImage",
                        src = "",
                        alt = "Limelight Feed",
                        styles = """
                            width: 100%;
                            height: 100%;
                        """.trimIndent()
                    )
                }
            )
        )
    }

    override fun onEnable() {
        isProxied = true
    }

    override fun onDisable() {
        isProxied = false
    }

    override fun onAttachEventLoop(eventLoop: FtcEventLoop) {

    }

}