package org.firstinspires.ftc.teamcode.configs

import com.bylazar.limelightproxy.ProxyPluginConfig
import com.bylazar.mylibrary.MyConfig


class ProxyConfig : ProxyPluginConfig(){
    override var isDev = true
    override var customString = "HIII!"
}

class MyConfig : MyConfig(){
    override var isDev = true
    override var test = "test"
}