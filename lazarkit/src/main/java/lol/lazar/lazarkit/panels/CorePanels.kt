package lol.lazar.lazarkit.panels

import android.content.Context
import android.view.Menu
import com.qualcomm.ftccommon.FtcEventLoop
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl
import com.qualcomm.robotcore.util.WebServer
import lol.lazar.lazarkit.panels.configurables.Configurables
import lol.lazar.lazarkit.panels.integration.MenuManager
import lol.lazar.lazarkit.panels.integration.OpModeRegistrar
import lol.lazar.lazarkit.panels.integration.UIManager
import lol.lazar.lazarkit.panels.server.LimelightServer
import lol.lazar.lazarkit.panels.server.Server
import lol.lazar.lazarkit.panels.server.Socket
import java.io.IOException

class CorePanels {
    var uiManager = UIManager()
    var menuManager = MenuManager(this::enable, this::disable)
    var opModeRegistrar = OpModeRegistrar(this::toggle)
    var opModeData = OpModeData({ data -> socket.sendOpModesList() })

    lateinit var server: Server
    lateinit var socket: Socket

    lateinit var limelightServer: LimelightServer

    var telemetryManager = TelemetryManager({ lines -> socket.sendTelemetry(lines) })


    fun attachWebServer(context: Context, webServer: WebServer) {
        try {
            server = Server(context)
            socket = Socket(this::initOpMode, this::startOpMode, this::stopOpMode)
            limelightServer = LimelightServer(context)
        } catch (e: IOException) {
            println("Failed to start: " + e.message)
        }
        limelightServer.startServer()

        if (!Preferences.isEnabled) return

        server.startServer()
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

        opModeManager?.unregisterListener(registrar)
        disable()

        uiManager.removeText()
    }

    fun enable() {
        if (Preferences.isEnabled) return
        Preferences.isEnabled = true
        uiManager.updateText()
        server.startServer()
        socket.startServer()
    }

    fun disable() {
        if (!Preferences.isEnabled) return
        Preferences.isEnabled = false
        uiManager.updateText()
        server.stopServer()
        socket.stopServer()
    }

    fun toggle() {
        if (Preferences.isEnabled) disable()
        else enable()
    }

    fun initOpMode(name: String) {
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