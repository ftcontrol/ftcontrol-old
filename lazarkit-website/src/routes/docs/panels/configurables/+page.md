# Configurables

## Example Configurables Files
<Tabs activeName="Kotlin Object" > 
  <TabPanel name ="Java Class" > 
    
```java
package org.firstinspires.ftc.teamcode.examples.configurables;

import java.util.List;
import java.util.Map;

import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable;
import com.bylazar.ftcontrol.panels.configurables.annotations.ConfigurableCustomType;
import com.bylazar.ftcontrol.panels.configurables.annotations.GenericValue;


@Configurable
public class TestJavaClass {
    public static int testInt = 1;
    public static long testLong = 1L;
    public static double testDouble = 1.0;
    public static float testFloat = 1.0f;
    public static String testString = "test!";
    public static boolean testBoolean = false;

    public enum TestEnum {
        TEST1,
        TEST2,
        TEST3
    }

    public static TestEnum testEnum = TestEnum.TEST1;
    public static int[] testArray = {1, 2, 3};
    public static List<Integer> testList = List.of(1, 2, 3);
    public static Map<String, Integer> testMap = Map.of("one", 1, "two", 2, "three", 3);

    @ConfigurableCustomType
    public static class CustomType {
        public final int testInt;
        public final String testString;

        public CustomType(int testInt, String testString) {
            this.testInt = testInt;
            this.testString = testString;
        }

        public int getTestInt() {
            return testInt;
        }

        public String getTestString() {
            return testString;
        }
    }

    public static CustomType testCustomType = new CustomType(1, "test!");

    @ConfigurableCustomType
    public static class NestedType {
        public final int testInt;
        public final String testString;
        public final CustomType testCustomType;

        public NestedType(int testInt, String testString, CustomType testCustomType) {
            this.testInt = testInt;
            this.testString = testString;
            this.testCustomType = testCustomType;
        }

        public int getTestInt() {
            return testInt;
        }

        public String getTestString() {
            return testString;
        }

        public CustomType getTestCustomType() {
            return testCustomType;
        }
    }

    public static NestedType testNestedType = new NestedType(1, "test!", new CustomType(2, "test2!"));

    public static class UnknownType {
        public final int testInt;

        public UnknownType(int testInt) {
            this.testInt = testInt;
        }

        public int getTestInt() {
            return testInt;
        }
    }

    public static UnknownType testUnknownType = new UnknownType(1);

    public static Object[] testRandomArray = new Object[]{
            1,
            1.0,
            1.0f,
            "test!",
            true,
            new CustomType(1, "test!"),
            new NestedType(1, "test!", new CustomType(2, "test2!")),
            new UnknownType(1),
            new int[]{1, 2, 3},
            Map.of("one", 1, "two", 2, "three", 3)
    };

    @ConfigurableCustomType
    public static class TParamClass<T> {
        public final T test;

        public TParamClass(T test) {
            this.test = test;
        }

        public T getTest() {
            return test;
        }
    }

    @GenericValue(tParam = Integer.class)
    public static TParamClass<Integer> testTParamClass = new TParamClass<Integer>(1);
}
```

  </TabPanel >
  <TabPanel name ="Kotlin Object" > 
    
```kotlin
package org.firstinspires.ftc.teamcode.examples.configurables

import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable
import com.bylazar.ftcontrol.panels.configurables.annotations.ConfigurableCustomType
import com.bylazar.ftcontrol.panels.configurables.annotations.GenericValue

@Configurable
object TestKotlinObject {
    @JvmField
    var testInt: Int = 1

    @JvmField
    var testLong: Long = 1L

    @JvmField
    var testDouble: Double = 1.0

    @JvmField
    var testFloat: Float = 1.0f

    @JvmField
    var testString: String = "test!"

    @JvmField
    var testBoolean: Boolean = false

    enum class TestEnum {
        TEST1,
        TEST2,
        TEST3
    }

    @JvmField
    var testEnum: TestEnum = TestEnum.TEST1

    @JvmField
    var testArray: Array<Int> = arrayOf(1, 2, 3)

    @JvmField
    var testList: List<Int> = listOf(1, 2, 3)

    @JvmField
    var testMap: Map<String, Int> = mapOf("one" to 1, "two" to 2, "three" to 3)

    @ConfigurableCustomType
    class CustomType(
        val testInt: Int,
        val testString: String
    )

    @JvmField
    var testCustomType: CustomType = CustomType(1, "test!")

    @ConfigurableCustomType
    class NestedType(
        val testInt: Int,
        val testString: String,
        val testCustomType: CustomType
    )

    @JvmField
    var testNestedType: NestedType = NestedType(1, "test!", CustomType(2, "test2!"))

    class UnknownType(
        val testInt: Int,
    )

    @JvmField
    var testUnknownType: UnknownType = UnknownType(1)

    @JvmField
    var testRandomArray = arrayOf(
        1,
        1.0,
        1.0f,
        "test!",
        true,
        CustomType(1, "test!"),
        NestedType(1, "test!", CustomType(2, "test2!")),
        UnknownType(1),
        arrayOf(
            1,
            2,
            3
        ),
        mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3
        )
    )

    @ConfigurableCustomType
    class TParamClass<T>(
        val test: T
    )

    @JvmField
    @field:GenericValue(Int::class)
    var testTParamClass = TParamClass(1)
}
```

  </TabPanel >
  <TabPanel name ="Kotlin Class" > 
    
```kotlin
package org.firstinspires.ftc.teamcode.examples.configurables

import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable
import com.bylazar.ftcontrol.panels.configurables.annotations.ConfigurableCustomType
import com.bylazar.ftcontrol.panels.configurables.annotations.GenericValue

@Configurable
class TestKotlinClass {
    companion object {
        @JvmField
        var testInt: Int = 1

        @JvmField
        var testLong: Long = 1L

        @JvmField
        var testDouble: Double = 1.0

        @JvmField
        var testFloat: Float = 1.0f

        @JvmField
        var testString: String = "test!"

        @JvmField
        var testBoolean: Boolean = false

        enum class TestEnum {
            TEST1,
            TEST2,
            TEST3
        }

        @JvmField
        var testEnum: TestEnum = TestEnum.TEST1

        @JvmField
        var testArray: Array<Int> = arrayOf(1, 2, 3)

        @JvmField
        var testList: List<Int> = listOf(1, 2, 3)

        @JvmField
        var testMap: Map<String, Int> = mapOf("one" to 1, "two" to 2, "three" to 3)

        @ConfigurableCustomType
        class CustomType(
            val testInt: Int,
            val testString: String
        )

        @JvmField
        var testCustomType: CustomType = CustomType(1, "test!")

        @ConfigurableCustomType
        class NestedType(
            val testInt: Int,
            val testString: String,
            val testCustomType: CustomType
        )

        @JvmField
        var testNestedType: NestedType = NestedType(1, "test!", CustomType(2, "test2!"))

        class UnknownType(
            val testInt: Int,
        )

        @JvmField
        var testUnknownType: UnknownType = UnknownType(1)

        @JvmField
        var testRandomArray = arrayOf(
            1,
            1.0,
            1.0f,
            "test!",
            true,
            CustomType(1, "test!"),
            NestedType(1, "test!", CustomType(2, "test2!")),
            UnknownType(1),
            arrayOf(
                1,
                2,
                3
            ),
            mapOf(
                "one" to 1,
                "two" to 2,
                "three" to 3
            )
        )

        @ConfigurableCustomType
        class TParamClass<T>(
            val test: T
        )

        @JvmField
        @field:GenericValue(Int::class)
        var testTParamClass = TParamClass(1)
    }
}
```

  </TabPanel >
</Tabs >