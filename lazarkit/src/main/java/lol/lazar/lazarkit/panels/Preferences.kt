package lol.lazar.lazarkit.panels

import android.content.Context
import android.content.SharedPreferences
import org.firstinspires.ftc.robotcore.internal.system.AppUtil

object Preferences {
    private var prefs: SharedPreferences? = null

    fun init(){
        val activity = AppUtil.getInstance().activity ?: return

        prefs = activity.getSharedPreferences("Lazar's Dashboard", Context.MODE_PRIVATE)

        isEnabled = prefs!!.getBoolean("autoEnable", true)
    }
    var isEnabled: Boolean = true
        set(value) {
            field = value
            prefs!!.edit().putBoolean("autoEnable", value).apply()
        }
}