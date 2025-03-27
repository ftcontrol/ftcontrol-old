package org.firstinspires.ftc.teamcode

import lol.lazar.lazarkit.panels.configurables.Configurable

@Configurable
class TestClass {
    companion object {
        @JvmField
        var testIntField = 42

        @JvmField
        var testDoubleField: Double = 42.5

        @JvmField
        var testArray = intArrayOf(1, 2, 3)

        @JvmField
        var testArray2 = arrayOf(1, 2, 3)

        data class CustomType(
            val name: String,
            val age: Int
        )

        data class NedtedType(
            val name: String,
            val custom: CustomType
        )

        @JvmField
        var crazyArray = arrayOf(
            1,
            2,
            3,
            CustomType("Alice", 25),
            CustomType("Bob", 35),
            "Hello, world!",
            TestEnum.VALUE1,
            true
        )

        @JvmField
        var testObject = CustomType("John", 30)

        @JvmField
        var nestedTestObject = NedtedType(
            "John",
            CustomType("Alice", 25)
        )

        @JvmField
        var testString = "Hello, world!"

        @JvmField
        var testObjectArray = arrayOf(
            CustomType("Alice", 25),
            CustomType("Bob", 35)
        )

        enum class TestEnum {
            VALUE1,
            VALUE2,
            VALUE3
        }

        @JvmField
        var testEnum = TestEnum.VALUE1
    }
}