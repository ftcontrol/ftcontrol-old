package com.bylazar.ftcontrol.panels

import android.content.Context
import android.view.Menu
import com.bylazar.ftcontrol.panels.configurables.Configurables
import com.bylazar.ftcontrol.panels.integration.MenuManager
import com.bylazar.ftcontrol.panels.integration.OpModeData
import com.bylazar.ftcontrol.panels.integration.OpModeRegistrar
import com.bylazar.ftcontrol.panels.integration.Preferences
import com.bylazar.ftcontrol.panels.integration.TelemetryManager
import com.bylazar.ftcontrol.panels.integration.UIManager
import com.bylazar.ftcontrol.panels.server.GenericProxy
import com.bylazar.ftcontrol.panels.server.GenericSocketProxy
import com.bylazar.ftcontrol.panels.server.GenericStreamingProxy
import com.bylazar.ftcontrol.panels.server.Server
import com.bylazar.ftcontrol.panels.server.Socket
import com.bylazar.ftcontrol.panels.server.TestLimelightServer
import com.qualcomm.ftccommon.FtcEventLoop
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl
import com.qualcomm.robotcore.util.WebServer
import java.io.IOException

class CorePanels {
    var uiManager = UIManager()
    var menuManager = MenuManager(this::enable, this::disable)
    var opModeRegistrar = OpModeRegistrar(this::toggle)
    var opModeData = OpModeData({ _ -> socket.sendOpModesList() })

    lateinit var server: Server
    lateinit var limelightProxy: GenericProxy
    lateinit var limelightFeedProxy: GenericStreamingProxy
    lateinit var limelightAPIProxy: GenericSocketProxy
    lateinit var socket: Socket

    lateinit var testLimelightServer: TestLimelightServer

    var telemetryManager =
        TelemetryManager({ lines, canvas, graph -> socket.sendTelemetry(lines, canvas, graph) })

    fun attachWebServer(context: Context, webServer: WebServer) {
        try {
            server = Server(context)
            limelightProxy = GenericProxy(5801, 5801, "172.29.0.1")
            limelightFeedProxy = GenericStreamingProxy(5800, 5800, "172.29.0.1")
            limelightAPIProxy = GenericSocketProxy(5805, 5805, "172.29.0.1")
            socket = Socket(this::initOpMode, this::startOpMode, this::stopOpMode)
            testLimelightServer = TestLimelightServer(context)
        } catch (e: IOException) {
            println("Failed to start: " + e.message)
        }
        testLimelightServer.startServer()

        if (!Preferences.isEnabled) return

        server.startServer()
        limelightProxy.startServer()
        limelightFeedProxy.startServer()
        limelightAPIProxy.startProxy()
        socket.startServer()

        Configurables.findConfigurables(context)
    }

    private var opModeManager: OpModeManagerImpl? = null

    fun attachEventLoop(eventLoop: FtcEventLoop, registrar: Panels) {
        opModeManager?.unregisterListener(registrar)

        opModeManager = eventLoop.opModeManager
        opModeManager?.registerListener(registrar)

        opModeData.init(eventLoop)
    }

    fun registerOpMode(manager: OpModeManager) = opModeRegistrar.registerOpMode(manager)
    fun createMenu(menu: Menu) = menuManager.createMenu(menu)

    fun start() {
        Preferences.init()

        if (Preferences.isEnabled) enable();

        uiManager.injectText()
    }

    fun close(registrar: Panels) {
        server.stopServer()
        socket.stopServer()
        limelightProxy.stopServer()
        limelightFeedProxy.stopServer()

        opModeManager?.unregisterListener(registrar)
        disable()

        uiManager.removeText()
    }

    fun enable() {
        if (Preferences.isEnabled) return
        Preferences.isEnabled = true
        uiManager.updateText()
        server.startServer()
        limelightProxy.startServer()
        limelightFeedProxy.startServer()
        limelightAPIProxy.startProxy()
        socket.startServer()
    }

    fun disable() {
        if (!Preferences.isEnabled) return
        Preferences.isEnabled = false
        uiManager.updateText()
        server.stopServer()
        limelightProxy.stopServer()
        limelightFeedProxy.stopServer()
        limelightAPIProxy.stopProxy()
        socket.stopServer()
    }

    fun toggle() {
        if (Preferences.isEnabled) disable()
        else enable()
    }

    fun initOpMode(name: String) {
        telemetryManager.resetGraphs()
        GlobalGamepad.reset()
        opModeManager?.initOpMode(name) ?: run {
            println("DASH: opModeManager is null")
        }
    }

    fun startOpMode() {
        opModeManager?.startActiveOpMode() ?: run {
            println("DASH: opModeManager is null")
        }
    }

    fun stopOpMode() {
        opModeManager?.stopActiveOpMode() ?: run {
            println("DASH: opModeManager is null")
        }
    }
}