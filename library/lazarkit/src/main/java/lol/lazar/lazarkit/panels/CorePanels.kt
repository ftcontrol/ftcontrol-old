package lol.lazar.lazarkit.panels

import android.content.Context
import android.view.Menu
import com.qualcomm.ftccommon.FtcEventLoop
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl
import com.qualcomm.robotcore.util.WebServer
import lol.lazar.lazarkit.panels.oldConfs.Configurables
import lol.lazar.lazarkit.panels.integration.MenuManager
import lol.lazar.lazarkit.panels.integration.OpModeData
import lol.lazar.lazarkit.panels.integration.OpModeRegistrar
import lol.lazar.lazarkit.panels.integration.Preferences
import lol.lazar.lazarkit.panels.integration.TelemetryManager
import lol.lazar.lazarkit.panels.integration.UIManager
import lol.lazar.lazarkit.panels.server.TestLimelightServer
import lol.lazar.lazarkit.panels.server.Server
import lol.lazar.lazarkit.panels.server.LimelightProxy
import lol.lazar.lazarkit.panels.server.Socket
import java.io.IOException

class CorePanels {
    var uiManager = UIManager()
    var menuManager = MenuManager(this::enable, this::disable)
    var opModeRegistrar = OpModeRegistrar(this::toggle)
    var opModeData = OpModeData({ _ -> socket.sendOpModesList() })

    lateinit var server: Server
    lateinit var limelightProxy: LimelightProxy
    lateinit var socket: Socket

    lateinit var testLimelightServer: TestLimelightServer

    var telemetryManager = TelemetryManager({ lines, canvas -> socket.sendTelemetry(lines, canvas) })

    fun attachWebServer(context: Context, webServer: WebServer) {
        try {
            server = Server(context)
            limelightProxy = LimelightProxy(context)
            socket = Socket(this::initOpMode, this::startOpMode, this::stopOpMode)
            testLimelightServer = TestLimelightServer(context)
        } catch (e: IOException) {
            println("Failed to start: " + e.message)
        }
        testLimelightServer.startServer()

        if (!Preferences.isEnabled) return

        server.startServer()
        limelightProxy.startServer()
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
        socket.startServer()
    }

    fun disable() {
        if (!Preferences.isEnabled) return
        Preferences.isEnabled = false
        uiManager.updateText()
        server.stopServer()
        limelightProxy.stopServer()
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