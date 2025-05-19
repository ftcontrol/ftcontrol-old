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
import com.bylazar.ftcontrol.panels.plugins.PluginManager
import com.bylazar.ftcontrol.panels.server.Server
import com.bylazar.ftcontrol.panels.server.Socket
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
    lateinit var socket: Socket

    var telemetryManager =
        TelemetryManager(
            { lines -> socket.sendLines(lines) },
            { graph -> socket.sendGraph(graph) },
            { canvas -> socket.sendCanvas(canvas) },
        )

    fun attachWebServer(context: Context, webServer: WebServer) {
        println("DASH: Preferences.isEnabled: ${Preferences.isEnabled}")

        try {
            server = Server(context)
            socket = Socket(this::initOpMode, this::startOpMode, this::stopOpMode)
        } catch (e: Exception) {
            println("DASH: Failed to start: " + e.message)
        }

        if (Preferences.isEnabled){
            socket.startServer()
            server.startServer()
        }

        try {
            println("DASH: Finding configurables")
            Configurables.findConfigurables(context)
        } catch(e: Exception){
            println("DASH: Failed to find configurables: ${e.message}")
            e.printStackTrace()
        } catch (t: Throwable) {
            println("DASH: Configurables Throwable caught: ${t::class.simpleName} - ${t.message}")
            t.printStackTrace()
        }

        try {
            PluginManager.loadPlugins(context)
        }catch(e: Exception){
            println("DASH: Failed to load plugins: ${e.message}")
            e.printStackTrace()
        }  catch (t: Throwable) {
            println("DASH: Plugins Throwable caught: ${t::class.simpleName} - ${t.message}")
            t.printStackTrace()
        }

        try {
            PluginManager.onRegister(this)
        }catch(e: Exception){
            println("DASH: Failed to register plugins: ${e.message}")
            e.printStackTrace()
        }  catch (t: Throwable) {
            println("DASH: Plugins Register Throwable caught: ${t::class.simpleName} - ${t.message}")
            t.printStackTrace()
        }
    }

    private var opModeManager: OpModeManagerImpl? = null

    fun attachEventLoop(eventLoop: FtcEventLoop, registrar: Panels) {
        opModeManager?.unregisterListener(registrar)

        opModeManager = eventLoop.opModeManager
        opModeManager?.registerListener(registrar)

        PluginManager.plugins.forEach { it.value.onAttachEventLoop(eventLoop) }

        opModeData.init(eventLoop)

    }

    fun registerOpMode(manager: OpModeManager) = opModeRegistrar.registerOpMode(manager)
    fun createMenu(menu: Menu) = menuManager.createMenu(menu)

    fun start() {
        if (Preferences.isEnabled) enable();

        uiManager.injectText()
    }

    fun close(registrar: Panels) {
        server.stopServer()
        socket.stopServer()

        opModeManager?.unregisterListener(registrar)
        disable()

        uiManager.removeText()
    }

    fun enable() {
        if (Preferences.isEnabled) return
        Preferences.isEnabled = true
        uiManager.updateText()
        socket.startServer()
        server.startServer()
        PluginManager.plugins.values.forEach { it.onEnable() }
    }

    fun disable() {
        if (!Preferences.isEnabled) return
        Preferences.isEnabled = false
        uiManager.updateText()
        server.stopServer()
        socket.stopServer()
        PluginManager.plugins.values.forEach { it.onDisable() }
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