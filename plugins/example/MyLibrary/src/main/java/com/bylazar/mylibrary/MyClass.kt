package com.bylazar.mylibrary

import com.bylazar.ftcontrol.panels.plugins.BasePluginConfig
import com.bylazar.ftcontrol.panels.plugins.ModContext
import com.bylazar.ftcontrol.panels.plugins.Page
import com.bylazar.ftcontrol.panels.plugins.PanelsPlugin
import com.bylazar.ftcontrol.panels.plugins.html.primitives.div
import com.bylazar.ftcontrol.panels.plugins.html.primitives.text
import com.qualcomm.ftccommon.FtcEventLoop

open class MyConfig : BasePluginConfig(){
    open var test = "test"
}

class MyClass : PanelsPlugin<MyConfig>(MyConfig()) {
    //    TODO: error handling
    override val globalVariables = mutableMapOf<String, () -> Any>(
        "test" to { 6 },
        "timestamp" to { System.currentTimeMillis() }
    )

    override val actions = mutableMapOf<String, () -> Unit>(
        "test" to { println("DASH: TEST ACTION") }
    )

    val iconSVG = """
        <svg
          width="100%"
          height="100%"
          class:disabled={!isActive}
          class:spin={isSelected}
          viewBox="0 0 13 14"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          xmlns:xlink="http://www.w3.org/1999/xlink"
          xml:space="preserve"
          style="fill-rule:evenodd;clip-rule:evenodd;stroke-linejoin:round;stroke-miterlimit:2;"
        >
          <path
            d="M0.478,8.19c-0.432,-1.595 -0.19,-3.216 0.551,-4.558c0.11,-0.194 0.067,-0.439 -0.103,-0.583c-0.192,-0.167 -0.455,-0.39 -0.711,-0.608c-0.173,-0.146 -0.25,-0.376 -0.2,-0.597c0.049,-0.22 0.216,-0.395 0.435,-0.454l5.055,-1.369c0.218,-0.059 0.451,0.008 0.605,0.174c0.075,0.08 0.125,0.177 0.148,0.28c0.025,0.11 0.019,0.226 -0.02,0.336l-1.533,4.358c-0.066,0.189 -0.221,0.334 -0.415,0.386c-0.193,0.052 -0.4,0.006 -0.553,-0.123l-0.75,-0.638c-0.114,-0.096 -0.266,-0.132 -0.411,-0.098c-0.11,0.026 -0.206,0.092 -0.27,0.182c-0.371,0.878 -0.463,1.881 -0.197,2.871l0.002,0.008c0.366,1.353 1.319,2.396 2.515,2.908c0.109,0.047 0.171,0.163 0.148,0.279c-0.06,0.297 -0.172,0.852 -0.24,1.189c-0.014,0.072 -0.059,0.134 -0.123,0.169c-0.045,0.026 -0.096,0.036 -0.147,0.031c-0.021,-0.002 -0.041,-0.007 -0.061,-0.014c-1.763,-0.679 -3.192,-2.159 -3.722,-4.12l-0.003,-0.009Zm6.045,4.524c-0.025,-0.11 -0.019,-0.226 0.02,-0.336l1.533,-4.359c0.066,-0.189 0.221,-0.333 0.415,-0.385c0.193,-0.053 0.4,-0.006 0.553,0.123l0.75,0.637c0.114,0.097 0.266,0.133 0.411,0.099c0.11,-0.027 0.206,-0.092 0.27,-0.182c0.371,-0.878 0.463,-1.881 0.197,-2.871l-0.002,-0.008c-0.367,-1.353 -1.322,-2.39 -2.515,-2.909c-0.109,-0.046 -0.171,-0.162 -0.148,-0.278c0.06,-0.297 0.172,-0.852 0.24,-1.189c0.014,-0.072 0.059,-0.134 0.123,-0.17c0.045,-0.025 0.096,-0.035 0.147,-0.03c0.021,0.002 0.041,0.007 0.061,0.014c1.763,0.679 3.191,2.159 3.722,4.12l0.003,0.009c0.432,1.595 0.19,3.216 -0.551,4.558c-0.11,0.194 -0.067,0.439 0.103,0.583c0.192,0.166 0.455,0.39 0.711,0.607c0.173,0.147 0.25,0.377 0.2,0.597c-0.049,0.221 -0.216,0.396 -0.435,0.455l-5.055,1.369c-0.218,0.059 -0.451,-0.008 -0.605,-0.174c-0.075,-0.08 -0.125,-0.177 -0.148,-0.28Z"
          />
        </svg>
        <style>
          svg {
            height: 20px;
            min-width: 20px;
            cursor: pointer;
            opacity: 0.5;
            transition:
              opacity var(--d2) ease-in-out,
              height var(--d2) ease-in-out,
              min-width var(--d2) ease-in-out;
          }

          svg.disabled {
            height: 0px;
            min-width: 0px;
            cursor: default;
          }
          svg.spin {
            opacity: 1;
          }

          path {
            fill: var(--text);
          }
        </style>
    """.trimIndent()

    override var id: String = "com.bylazar.myplugin"
    override val name: String = "Lazar's Example Plugin"
    override fun onRegister(context: ModContext) {
        println("DASH: ran internal plugin register")
        createPage("1", "Test Page 1")

        createPage("2", "Test Page 2")
        createPage(
            Page(
                id="3",
                title = "Test HTML",
                html = text(
                    //language=HTML
                    """
                    <h1>Test Page 3</h1>
                    
                    <p style="color: var(--primary)">Primary colored</p>
                    
                    <button onclick="alert('Hello World!')">Click Me!</button>
                    """.trimIndent()
                )
            )
        )
        createPage(
            Page(
                id="4",
                title = "Test HTML Builders",
                html = div {
                    p(styles = "color:red;") { dynamic("timestamp") }
                    p(styles = "color:blue;") { dynamic("timestamp2") }
                    h1 {
                        text("Heading")
                        text("Heading2")
                        dynamic("test")
                    }
                    button(action = "test") {
                        text("Run SSA")
                    }
                    button(styles = """
                        all: unset;
                        cursor: pointer;
                    """.trimIndent()){
                        text(iconSVG)
                    }
                }
            ))
    }

    override fun onEnable() {
    }

    override fun onDisable() {
    }

    override fun onAttachEventLoop(eventLoop: FtcEventLoop) {
    }
}