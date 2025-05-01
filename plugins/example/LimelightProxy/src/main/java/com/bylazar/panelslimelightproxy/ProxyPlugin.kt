package com.bylazar.panelslimelightproxy

import com.bylazar.ftcontrol.panels.plugins.ModContext
import com.bylazar.ftcontrol.panels.plugins.Page
import com.bylazar.ftcontrol.panels.plugins.PanelsPlugin
import com.bylazar.ftcontrol.panels.plugins.html.primitives.div
import com.bylazar.panelslimelightproxy.proxies.GenericProxy
import com.bylazar.panelslimelightproxy.proxies.GenericSocketProxy
import com.bylazar.panelslimelightproxy.proxies.GenericStreamingProxy

class ProxyPlugin : PanelsPlugin() {
    override val globalVariables: Map<String, () -> Any> = mapOf(
        "isEnabled" to { isEnabled },
    )
    override val actions: Map<String, () -> Unit> = mapOf(
        "toggle" to {
            isEnabled = !isEnabled
        },
    )
    override var id: String = "com.bylazar.limelightproxy"
    override val name: String = "Limelight Proxy"

    lateinit var limelightProxy: GenericProxy
    lateinit var limelightFeedProxy: GenericStreamingProxy
    lateinit var limelightWebsocketProxy: GenericSocketProxy
    lateinit var limelightAPIProxy: GenericProxy

    var isEnabled = false
        set(value) {
            when (value) {
                true -> {
                    limelightProxy.startServer()
                    limelightFeedProxy.startServer()
                    limelightWebsocketProxy.startProxy()
                    limelightAPIProxy.startServer()
                }

                false -> {
                    limelightProxy.stopServer()
                    limelightFeedProxy.stopServer()
                    limelightWebsocketProxy.stopProxy()
                    limelightAPIProxy.stopServer()
                }
            }
            field = value
        }

    override fun onRegister(context: ModContext) {
//        limelightProxy = GenericProxy(5801, 5801, "172.29.0.1")
        limelightProxy = GenericProxy(5801, 3331, "localhost")

        limelightFeedProxy = GenericStreamingProxy(5800, 5800, "172.29.0.1")
        limelightWebsocketProxy = GenericSocketProxy(5805, 5805, "172.29.0.1")
        limelightAPIProxy = GenericProxy(5807, 5807, "172.29.0.1")

        isEnabled = true

        createPage(
            Page(
            title = "Limelight Dash",
            html = div {
                p {
                    text("isEnabled: ")
                    dynamic("isEnabled")
                }
                button(action = "toggle") {
                    text("Toggle")
                }
                text("<iframe src=\"http://localhost:5801/\" title=\"Limelight Dashboard\"> </iframe>")
            }
        ))
    }

    override fun onEnable() {
        isEnabled = true
    }

    override fun onDisable() {
        isEnabled = false
    }

}