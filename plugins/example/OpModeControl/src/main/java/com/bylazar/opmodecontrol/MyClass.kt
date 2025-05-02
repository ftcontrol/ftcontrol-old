package com.bylazar.opmodecontrol

import com.bylazar.ftcontrol.panels.plugins.BasePluginConfig
import com.bylazar.ftcontrol.panels.plugins.ModContext
import com.bylazar.ftcontrol.panels.plugins.Page
import com.bylazar.ftcontrol.panels.plugins.PanelsPlugin
import com.bylazar.ftcontrol.panels.plugins.html.primitives.div

class OpModeControlConfig : BasePluginConfig()

class OpModeControl : PanelsPlugin<OpModeControlConfig>(OpModeControlConfig()) {
    override val globalVariables = mapOf<String, () -> Any>(

    )

    override val actions = mapOf<String, () -> Unit>(
    )


    override var id: String = "com.bylazar.opmodecontrol"
    override val name: String = "OpMode Control"
    override fun onRegister(context: ModContext) {
        createPage(
            Page(
                id = "simpleControls",
                title = "Simple OpMode Controls",
                html = div()
            )
        )
    }

    override fun onEnable() {

    }

    override fun onDisable() {
    }
}