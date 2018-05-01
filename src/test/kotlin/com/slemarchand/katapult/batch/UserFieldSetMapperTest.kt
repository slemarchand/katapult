package com.slemarchand.katapult.batch

import com.slemarchand.katapult.batch.model.Phone
import com.slemarchand.katapult.batch.model.User
import info.solidsoft.mockito.java8.LambdaMatcher.argLambda
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.batch.item.file.transform.DefaultFieldSet
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.springframework.batch.item.file.transform.FieldSet
import org.springframework.beans.NotWritablePropertyException

class UserFieldSetMapperTest {

    lateinit var mapper: UserFieldSetMapper

    @BeforeEach
    open fun init() {
        mapper = UserFieldSetMapper()
    }

    @Test
    fun testMapFieldSet () {

        val fs = DefaultFieldSet(arrayOf(), arrayOf())


       val mapper = mock(UserFieldSetMapper::class.java)

        fun anyFieldSet(): FieldSet {
            any<FieldSet>()
            return DefaultFieldSet(arrayOf(), arrayOf())
        }

        `when`(mapper.extractExpandoAttributes(anyFieldSet())).thenReturn(mutableMapOf())
        `when`(mapper.extractAllBeanCollectionFields(anyFieldSet())).thenReturn(mutableMapOf())
        `when`(mapper.removeFields(anyFieldSet(), argLambda({ _: Any -> anyBoolean() }))).thenReturn(fs)
        `when`(mapper.mapBeanCollection(mutableMapOf(), Phone::class.java)).thenReturn(listOf())
        `when`(mapper.superMapFieldSet(anyFieldSet())).thenReturn(User())

        `when`(mapper.mapFieldSet(anyFieldSet())).thenCallRealMethod()

        val user = mapper.mapFieldSet(fs)

        verify(mapper).extractExpandoAttributes(anyFieldSet())
        verify(mapper).extractAllBeanCollectionFields(anyFieldSet())
        verify(mapper).removeFields(anyFieldSet(), any())
        verify(mapper).superMapFieldSet(anyFieldSet())
        verify(mapper).mapBeanCollection(any(), eq(Phone::class.java))

    }

    @Test
    fun testMapFieldSet_end_to_end () {

        val fs = DefaultFieldSet(arrayOf(
            "boby", "boby@hello.zz","someExpandoValue","0102030405"
        ), arrayOf(
            "screenName", "emailAdress","expando:someCustomField", "phones[1].number"
        ))

        val user = mapper.mapFieldSet(fs)

        assertEquals("boby", user.screenName)
        assertEquals("boby@hello.zz", user.emailAddress)
        assertNotNull(user.expandoBridgeAttributes)
        assertEquals("someExpandoValue", user.expandoBridgeAttributes["someCustomField"])
        assertNotNull(user.phones)
        assertEquals(1, user.phones.size)
        assertNotNull(user.phones[0])
        assertEquals("0102030405", user.phones[0].number)
    }

    fun <T>eq(value: T): T  {
        var result = ArgumentMatchers.eq(value)
        if(result == null) {
            result = value
        }
        return result
    }

    @Test
    fun testMapFieldSet_end_to_end_bad_field_names () {

        fun testBadFieldName(badFieldName: String) {
            try {
                mapper.mapFieldSet(DefaultFieldSet(arrayOf(
                        "boby", "boby@hello.zz", "value"
                ), arrayOf(
                        "screenName", "emailAdress", badFieldName
                )))

                fail<Any>("<${badFieldName}> field name  should be rejected")

            } catch (e: NotWritablePropertyException) {
                /* Expected exception */
            }
        }

        testBadFieldName("randomFieldName")

        testBadFieldName("phones[9].unknown")

        testBadFieldName("phones[fffff")

        testBadFieldName("phones[7]df")
    }

    @Test
    fun testSuperMapFieldSet () {

        val names = arrayOf(
                "screenName",
                "emailAddress"
                )

        val tokens = arrayOf(
                "edgar",
                "edgar@motorcycle.zz")

        val user = mapper.superMapFieldSet(DefaultFieldSet(
                tokens,
                names))

        assertNotNull(user)
        assertEquals("edgar", user.screenName)
        assertEquals("edgar@motorcycle.zz", user.emailAddress)
    }

    @Test
    fun testExtractAllBeanCollectionFields () {

        val names = arrayOf(
                "phone[0].extension", "phone[0].number",
                "phone[1].extension", "phone[1].number",
                "phone[3].extension", "phone[3].number",
                "anyComposite[anyKey].someField",
                "screenName",
                "emailAddress")

        val tokens = arrayOf(
                "+33", "6 54 32 10 00",
                null, "01 02 03 04 05",
                "+44", "1 23 45 67 89",
                "foobar",
                "edgar",
                "edgar@motorcycle.zz")

        val data = mapper.extractAllBeanCollectionFields(DefaultFieldSet(
                tokens,
                names))

        assertNotNull(data)
        assertNotNull(data.get("phone"))
        assertNotNull(data.get("phone")!!.get("0"))
        assertNotNull(data.get("phone")!!.get("1"))
        assertNotNull(data.get("phone")!!.get("3"))
        assertNotNull(data.get("anyComposite"))
        assertNotNull(data.get("anyComposite")!!.get("anyKey"))
        assertEquals("+33", data.get("phone")!!.get("0")!!.get("extension"))
        assertEquals("6 54 32 10 00", data.get("phone")!!.get("0")!!.get("number"))
        assertNull(data.get("phone")!!.get("1")!!.get("extension"))
        assertEquals("01 02 03 04 05", data.get("phone")!!.get("1")!!.get("number"))
        assertEquals("+44", data.get("phone")!!.get("3")!!.get("extension"))
        assertEquals("1 23 45 67 89", data.get("phone")!!.get("3")!!.get("number"))
        assertEquals("foobar", data.get("anyComposite")!!.get("anyKey")!!.get("someField"))
    }

    @Test
    fun testExtractAllBeanCollectionFields_empty_field_set () {

        val data = mapper.extractAllBeanCollectionFields(DefaultFieldSet(
                arrayOf(),
                arrayOf()))

        assertEquals(0, data.size)
    }

        @Test
    open fun testMapBeanCollection () {

        val input : MutableMap<String, MutableMap<String, String>> = mutableMapOf(
            "00" to mutableMapOf(
                "extension" to "+33",
                "number" to "000"
            ),
            "02" to mutableMapOf(
                "extension" to "+44",
                "number" to "222"),
            "11" to mutableMapOf(
                "extension" to "+41",
                "number" to "111"
            )
        )

        val list = mapper.mapBeanCollection(input, Phone::class.java)

        assertEquals("+33", list.get(0).extension)
        assertEquals("000", list.get(0).number)

        assertEquals("+44", list.get(1).extension)
        assertEquals("222", list.get(1).number)

        assertEquals("+41", list.get(2).extension)
        assertEquals("111", list.get(2).number)
    }

    @Test
    open fun testMapBeanCollection_some_empty_fields () {

        val input : MutableMap<String, MutableMap<String, String>> = mutableMapOf(
                "0" to mutableMapOf(
                        "extension" to "",
                        "number" to ""
                ),
                "02" to mutableMapOf(
                        "extension" to "+44",
                        "number" to "222")
        )

        val list = mapper.mapBeanCollection(input, Phone::class.java)

        assertEquals(1, list.size)

        assertEquals("+44", list.get(0).extension)
        assertEquals("222", list.get(0).number)
    }

    @Test
    open fun testMapBeanCollection_null_parameters () {

        val list = mapper.mapBeanCollection(null, Phone::class.java)

        assertEquals(0, list.size)
    }

    @Test
    open fun testMapBeanCollection_null_entry () {

        val input : MutableMap<String, MutableMap<String, String>> = object: HashMap<String, MutableMap<String, String>>(){

            override fun get(key: String): MutableMap<String, String>? {
                return null
            }
        }

        input.put("0", mutableMapOf())
        input.put("1", mutableMapOf())

        val list = mapper.mapBeanCollection(input, Phone::class.java)

        assertEquals(0, list.size)
    }

    @Test
    open fun testExtractExpandoAttributes () {

        val fs = DefaultFieldSet(
                arrayOf("elton","val1", "val2"),
                arrayOf("screenName", "expando:name1","expando:name2"))

        val attrs = mapper.extractExpandoAttributes(fs)

        assertEquals(2, attrs.size)
        assertEquals("val1", attrs.get("name1"))
        assertEquals("val2", attrs.get("name2"))
    }

    @Test
    open fun testExtractExpandoAttributes_empty_field_set () {

        val fs = DefaultFieldSet(
                arrayOf(),
                arrayOf())

        val attrs = mapper.extractExpandoAttributes(fs)

        assertEquals(0, attrs.size)
    }

    @Test
    open fun testExtractExpandoAttributes_empty_key () {

        val fs = DefaultFieldSet(
                arrayOf("elton"),
                arrayOf(""))

        val attrs = mapper.extractExpandoAttributes(fs)

        assertEquals(0, attrs.size)
    }

    @Test
    open fun testRemoveFields_dot_notation_fields () {

        val fs = DefaultFieldSet(
                arrayOf("elton","0102030405","j"),
                arrayOf("screenName", "phone[0].number","lastName"))

        val newfs = mapper.removeFields(fs, { name, value -> name.contains('.') })

        assertArrayEquals(arrayOf("screenName","lastName"), newfs!!.names)
        assertArrayEquals(arrayOf("elton","j"), newfs!!.values)

    }

    @Test
    open fun testRemoveFields_no_dot_notation_field () {

        val fs = DefaultFieldSet(
                arrayOf("elton","j"),
                arrayOf("screenName","lastName"))

        val newfs = mapper.removeFields(fs, { name, value -> name.contains('.') })

        assertArrayEquals(fs.names, newfs!!.names)
        assertArrayEquals(fs.values, newfs!!.values)

    }

    @Test
    open fun testRemoveFields_nothing_to_remove () {

        val fs = DefaultFieldSet(
                arrayOf("elton","j"),
                arrayOf("screenName","lastName"))

        val newfs = mapper.removeFields(fs, null)

        assertArrayEquals(fs.names, newfs!!.names)
        assertArrayEquals(fs.values, newfs!!.values)

    }

    @Test
    open fun testRemoveFields_empty_field_set () {

        val fs = DefaultFieldSet(
                arrayOf(),
                arrayOf())

        val newfs = mapper.removeFields(fs, null)

        assertArrayEquals(fs.names, newfs!!.names)
        assertArrayEquals(fs.values, newfs!!.values)

    }
}