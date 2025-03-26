package lol.lazar.lazarkit.panels.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta.Flavor

@Serializable
@SerialName("opModeInfo")
data class OpModeInfo(
    var name: String,
    var group: String,
    var flavour: Flavor
) : JSONData()

@Serializable
@SerialName("activeOpMode")
data class ActiveOpMode(
    var opMode: OpModeInfo,
    var status: String
) : JSONData()

@Serializable
@SerialName("getOpmodes")
data object GetOpModesRequest : JSONData()

@Serializable
@SerialName("opmodes")
data class ReceivedOpModes(
    var opModes: List<OpModeInfo>
) : JSONData()

@Serializable
@SerialName("getActiveOpMode")
data object GetActiveOpModeRequest : JSONData()

@Serializable
@SerialName("initOpMode")
data class InitOpModeRequest(
    var opModeName: String
) : JSONData()

@Serializable
@SerialName("startActiveOpMode")
data object StartActiveOpModeRequest : JSONData()

@Serializable
@SerialName("stopActiveOpMode")
data object StopActiveOpModeRequest : JSONData()