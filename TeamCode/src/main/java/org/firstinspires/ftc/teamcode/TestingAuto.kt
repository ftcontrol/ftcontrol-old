package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import lol.lazar.lazarkit.panels.Panels
import lol.lazar.lazarkit.panels.configurables.annotations.Configurable
import lol.lazar.lazarkit.panels.configurables.annotations.ConfigurableCustomType

@Configurable
@Autonomous(name = "Testing Auto OpMode", group = "Dashboard")
class TestingAuto : OpMode(
) {
    companion object {
        @JvmField
        var intValue: Int = 0

        @JvmField
        var longValue: Long = 0L

        @JvmField
        var doubleValue: Double = 0.0

        @JvmField
        var floatValue: Float = 0.0f

        @JvmField
        var booleanValue: Boolean = false

        @JvmField
        var stringValue: String = "test"

        enum class CustomEnum {
            TEST,
            TEST2,
            TEST3,
            TEST4
        }

        @JvmField
        var customEnum: CustomEnum = CustomEnum.TEST

        @ConfigurableCustomType
        class CustomData(
            @JvmField var intValue: Int = 0,
            @JvmField var stringValue: String
        ){
            override fun toString(): String {
                return "CustomData(intValue=$intValue, stringValue='$stringValue')"
            }
        }

        @ConfigurableCustomType
        class CustomNestedData(
            @JvmField var customData: CustomData,
            @JvmField var stringValue: String
        ){
            override fun toString(): String {
                return "CustomNestedData(customData=$customData, stringValue='$stringValue')"
            }
        }

        @JvmField
        var customData = CustomData(
            intValue = 5,
            stringValue = "test"
        )

        @JvmField
        var customNestedData = CustomNestedData(
            customData = CustomData(
                intValue = 5,
                stringValue = "test"
            ),
            stringValue = "test"
        )

        @JvmField
        var testArray = intArrayOf(1, 2, 3)
    }

    var panelsTelemetry = Panels.getTelemetry()

    override fun init() {
        panelsTelemetry.debug("Hi, init was ran!")
        panelsTelemetry.update(telemetry)
    }

    override fun start() {
    }

    override fun loop() {
        panelsTelemetry.debug("Hi, loop was ran!")
        panelsTelemetry.debug("Int is $intValue")
        panelsTelemetry.debug("Long is $longValue")
        panelsTelemetry.debug("Double is $doubleValue")
        panelsTelemetry.debug("Float is $floatValue")
        panelsTelemetry.debug("Boolean is $booleanValue")
        panelsTelemetry.debug("String is $stringValue")
        panelsTelemetry.debug("Enum is $customEnum")
        panelsTelemetry.debug("Custom Data is $customData")
        panelsTelemetry.debug("Custom Nested Data is $customNestedData")

        panelsTelemetry.debug("Array is ${testArray.joinToString()}")

        panelsTelemetry.update(telemetry)
    }
}