package lol.lazar.lazarkit.panels.integration

import android.view.Menu
import android.view.MenuItem

class MenuManager(
    private val enable: () -> Unit,
    private val disable: () -> Unit
) {
    private val enableMenuItems: MutableList<MenuItem> = arrayListOf()
    private val disableMenuItems: MutableList<MenuItem> = arrayListOf()

    fun createMenu(menu: Menu) {
        val enable = menu.add(Menu.NONE, Menu.NONE, 700, "Enable Panels")
        val disable = menu.add(Menu.NONE, Menu.NONE, 700, "Disable Panels")

        enable.setVisible(!Preferences.isEnabled)
        disable.setVisible(Preferences.isEnabled)

        synchronized(enableMenuItems) {
            enableMenuItems.add(enable)
        }

        synchronized(disableMenuItems) {
            disableMenuItems.add(disable)
        }

        enable.setOnMenuItemClickListener {
            enable()
            synchronized(enableMenuItems) {
                for (menuItem in enableMenuItems) {
                    menuItem.setVisible(false)
                }
            }

            synchronized(disableMenuItems) {
                for (menuItem in disableMenuItems) {
                    menuItem.setVisible(true)
                }
            }
            true
        }

        disable.setOnMenuItemClickListener {
            disable()
            synchronized(enableMenuItems) {
                for (menuItem in enableMenuItems) {
                    menuItem.setVisible(true)
                }
            }

            synchronized(disableMenuItems) {
                for (menuItem in disableMenuItems) {
                    menuItem.setVisible(false)
                }
            }
            true
        }
    }
}