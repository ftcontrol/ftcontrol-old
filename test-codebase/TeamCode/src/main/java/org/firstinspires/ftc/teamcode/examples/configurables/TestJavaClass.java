package org.firstinspires.ftc.teamcode.examples.configurables;

import java.util.List;
import java.util.Map;

import lol.lazar.lazarkit.panels.configurables.annotations.Configurable;
import lol.lazar.lazarkit.panels.configurables.annotations.ConfigurableCustomType;

@Configurable
public class TestJavaClass {
    public int testInt = 1;
    public long testLong = 1L;
    public double testDouble = 1.0;
    public float testFloat = 1.0f;
    public String testString = "test!";
    public boolean testBoolean = false;

    public enum TestEnum {
        TEST1,
        TEST2,
        TEST3
    }

    public TestEnum testEnum = TestEnum.TEST1;
    public int[] testArray = {1, 2, 3};
    public List<Integer> testList = List.of(1, 2, 3);
    public Map<String, Integer> testMap = Map.of("one", 1, "two", 2, "three", 3);

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

    public CustomType testCustomType = new CustomType(1, "test!");

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

    public NestedType testNestedType = new NestedType(1, "test!", new CustomType(2, "test2!"));

    public static class UnknownType {
        public final int testInt;

        public UnknownType(int testInt) {
            this.testInt = testInt;
        }

        public int getTestInt() {
            return testInt;
        }
    }

    public UnknownType testUnknownType = new UnknownType(1);

    public Object[] testRandomArray = new Object[]{
            1,
            1.0,
            1.0f,
            "test!",
            true,
            new CustomType(1, "test!"),
            new NestedType(1, "test!", new CustomType(2, "test2!")),
            new UnknownType(1),
            new int[]{1, 2, 3}
    };

    public static class TParamClass<T> {
        public final T test;

        public TParamClass(T test) {
            this.test = test;
        }

        public T getTest() {
            return test;
        }
    }

    public TParamClass<Integer> testTParamClass = new TParamClass<>(1);
}
