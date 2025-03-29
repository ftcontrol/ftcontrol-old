package org.firstinspires.ftc.teamcode

import lol.lazar.lazarkit.panels.configurables.Configurable
import lol.lazar.lazarkit.panels.configurables.ConfigurableCustomType

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

        @ConfigurableCustomType
        data class CustomType(
            @JvmField var name: String,
            @JvmField var age: Int
        )

        @ConfigurableCustomType
        data class NestedType(
            @JvmField var name: String,
            @JvmField var custom: CustomType,
            @JvmField var enumClass: TestEnum = TestEnum.VALUE1
        )

        data class UnknownType(
            @JvmField var name: String,
            @JvmField var age: Int
        )

        @JvmField
        var unknownObject = UnknownType("John", 30)

        @JvmField
        var crazyArray = arrayOf(
            1,
            2,
            3,
            CustomType("Alice", 25),
            CustomType("Bob", 35),
            NestedType(
                "John",
                CustomType("Alice", 25)
            ),
            UnknownType("John", 30),
            "Hello, world!",
            TestEnum.VALUE1,
            true
        )

        @JvmField
        var testObject = CustomType("John", 30)

        @JvmField
        var nestedTestObject = NestedType(
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