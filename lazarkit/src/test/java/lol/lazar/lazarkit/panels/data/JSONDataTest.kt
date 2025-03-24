package lol.lazar.lazarkit.panels.data

import org.junit.Assert.assertEquals
import org.junit.Test

class JSONDataTest {

    @Test
    fun testSerializeSingleObject() {
        val testObject = TestObject(data = "example")

        val jsonString = testObject.toJson()

        assertEquals("""{"kind":"test","data":"example"}""", jsonString)
    }

    @Test
    fun testDeserializeSingleObject() {
        val jsonString = """{"kind":"test","data":"example"}"""

        val deserializedObject = jsonString.fromJsonToObject<TestObject>()

        assertEquals(TestObject(data = "example"), deserializedObject)
    }

    @Test
    fun testSerializeListOfObjects() {
        val testArray = listOf(
            TestObject(data = "example1"),
            TestObject(data = "example2")
        )

        val jsonString = testArray.toJson()

        assertEquals(
            """[{"kind":"test","data":"example1"},{"kind":"test","data":"example2"}]""",
            jsonString
        )
    }

    @Test
    fun testDeserializeListOfObjects() {
        val jsonString = """[{"kind":"test","data":"example1"},{"kind":"test","data":"example2"}]"""

        val deserializedArray = jsonString.fromJsonToList<TestObject>()

        assertEquals(
            listOf(
                TestObject(data = "example1"),
                TestObject(data = "example2")
            ),
            deserializedArray
        )
    }
}