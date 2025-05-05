package com.bylazar.ftcontrol.panels.integration

import android.content.Context
import android.content.SharedPreferences
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import androidx.core.content.edit

object Preferences {
    private var prefs: SharedPreferences? = null

    var isEnabled: Boolean = true
        set(value) {
            prefs!!.edit() { putBoolean("autoEnable", value) }
            field = value
        }
        get() {
            if(prefs == null){
                val activity = AppUtil.getInstance().activity ?: return true

                prefs = activity.getSharedPreferences("FTControl Panels", Context.MODE_PRIVATE)
            }

            val prefsInstance = prefs ?: error("DASH: Preferences not initialized!")
            return prefsInstance.getBoolean("autoEnable", true)
        }
}

data class UserPreferences(
    var colorTheme: String = ""
)