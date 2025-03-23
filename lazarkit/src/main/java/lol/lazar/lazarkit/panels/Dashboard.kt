package lol.lazar.lazarkit.panels

import android.content.Context
import android.view.Menu
import com.qualcomm.ftccommon.FtcEventLoop
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl
import com.qualcomm.robotcore.util.WebServer
import org.lazarkit.panels.GlobalGamepad
import org.lazarkit.panels.OpModeData
import org.lazarkit.panels.Preferences
import org.lazarkit.panels.integration.MenuManager
import org.lazarkit.panels.integration.OpModeRegistrar
import org.lazarkit.panels.integration.UIManager
import org.lazarkit.panels.server.LimelightServer
import org.lazarkit.panels.server.Server
import org.lazarkit.panels.server.Socket
import java.io.IOException

class Dashboard {
    var uiManager = UIManager()
    var menuManager = MenuManager(this::enable, this::disable)
    var opModeRegistrar = OpModeRegistrar(this::toggle)
    var opModeData = OpModeData()

    lateinit var server: Server
    lateinit var socket: Socket

    lateinit var limelightServer: LimelightServer


    fun attachWebServer(context: Context, webServer: WebServer) {
        try {
            server = Server(context)
            socket = Socket(this::initOpMode, this::startOpMode, this::stopOpMode)
            limelightServer = LimelightServer(context)
        } catch (e: IOException) {
            println("Failed to start: " + e.message)
        }
        limelightServer.startServer()

        if(!Preferences.isEnabled) return

        server.startServer()
        socket.startServer()

    }

    private var opModeManager: OpModeManagerImpl? = null

    fun attachEventLoop(eventLoop: FtcEventLoop, registrar: DashboardRegistrar) {
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

    fun close(registrar: DashboardRegistrar) {
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

    fun initOpMode(name: String){
        GlobalGamepad.reset()
        opModeManager?.initOpMode(name) ?: run {
            println("DASH: opModeManager is null")
        }
    }

    fun startOpMode(){
        opModeManager?.startActiveOpMode() ?: run {
            println("DASH: opModeManager is null")
        }
    }

    fun stopOpMode(){
        opModeManager?.stopActiveOpMode() ?: run {
            println("DASH: opModeManager is null")
        }
    }
}