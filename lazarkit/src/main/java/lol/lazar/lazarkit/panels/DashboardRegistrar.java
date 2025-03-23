package lol.lazar.lazarkit.panels;

import android.content.Context;
import android.view.Menu;

import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;
import com.qualcomm.robotcore.util.WebHandlerManager;
import com.qualcomm.robotcore.util.WebServer;

import org.firstinspires.ftc.ftccommon.external.OnCreate;
import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop;
import org.firstinspires.ftc.ftccommon.external.OnCreateMenu;
import org.firstinspires.ftc.ftccommon.external.OnDestroy;
import org.firstinspires.ftc.ftccommon.external.WebHandlerRegistrar;
import org.firstinspires.ftc.ftccommon.internal.FtcRobotControllerWatchdogService;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class DashboardRegistrar implements OpModeManagerImpl.Notifications {
    private final Dashboard dashboard = new Dashboard();
    private static DashboardRegistrar registrar;

    public static DashboardRegistrar getInstance() {
        return registrar;
    }

    public void send(){
        dashboard.socket.sendTest();
    }
    
    @OpModeRegistrar
    public static void registerOpMode(OpModeManager manager) {
        if (registrar != null) {
            registrar.internalRegisterOpMode(manager);
        }
    }

    @OnCreate
    public static void start(Context context) {
        if (registrar == null) {
            registrar = new DashboardRegistrar();
        }
    }

    @WebHandlerRegistrar
    public static void attachWebServer(Context context, WebHandlerManager manager) {
        if (registrar != null) {
            registrar.internalAttachWebServer(context, manager.getWebServer());
        }
    }

    @OnCreateEventLoop
    public static void attachEventLoop(Context context, FtcEventLoop eventLoop) {
        if (registrar != null) {
            registrar.internalAttachEventLoop(eventLoop);
        }
    }

    @OnCreateMenu
    public static void populateMenu(Context context, Menu menu) {
        if (registrar != null) {
            registrar.internalPopulateMenu(menu);
        }
    }

    @OnDestroy
    public static void stop(Context context) {
        if (!FtcRobotControllerWatchdogService.isLaunchActivity(
                AppUtil.getInstance().getRootActivity())) {
            return;
        }

        if (registrar != null) {
            registrar.close();
            registrar = null;
        }
    }

    private DashboardRegistrar() {
        dashboard.start();
    }

    private void internalAttachWebServer(Context context, WebServer webServer) {
        dashboard.attachWebServer(context, webServer);
    }

    private void internalAttachEventLoop(FtcEventLoop eventLoop) {
        dashboard.attachEventLoop(eventLoop, this);
    }

    private void internalPopulateMenu(Menu menu) {
        dashboard.createMenu(menu);
    }

    private void internalRegisterOpMode(OpModeManager manager) {
        dashboard.registerOpMode(manager);
    }

    private void close() {
        dashboard.close(this);
    }

    private void internalOnOpModeInit(OpMode opMode) {
        dashboard.getOpModeData().setInit(opMode);
        dashboard.socket.broadcastActiveOpMode();
    }

    private void internalOnOpModeStart(OpMode opMode) {
        dashboard.getOpModeData().setRunning(opMode);
        dashboard.socket.broadcastActiveOpMode();
    }

    private void internalOnOpModeStop(OpMode opMode) {
        dashboard.getOpModeData().setStopped(opMode);
        dashboard.socket.broadcastActiveOpMode();
    }

    @Override
    public void onOpModePreInit(OpMode opMode) {
        if (registrar != null) {
            registrar.internalOnOpModeInit(opMode);
        }
    }

    @Override
    public void onOpModePreStart(OpMode opMode) {
        if (registrar != null) {
            registrar.internalOnOpModeStart(opMode);
        }
    }

    @Override
    public void onOpModePostStop(OpMode opMode) {
        if (registrar != null) {
            registrar.internalOnOpModeStop(opMode);
        }
    }
}
