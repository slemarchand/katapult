package com.slemarchand.katapult.batch

import com.slemarchand.katapult.batch.model.Phone
import com.slemarchand.katapult.batch.model.User
import info.solidsoft.mockito.java8.LambdaMatcher.argLambda
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.batch.item.file.transform.DefaultFieldSet
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet
import javax.print.DocFlavor

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

        `when`(mapper.extractExpandoAttributes(any())).thenReturn(mutableMapOf())
        `when`(mapper.extractAllBeanCollectionFields(any())).thenReturn(mutableMapOf())
        `when`(mapper.removeFields(any(), argLambda({ _: Any -> anyBoolean() }))).thenReturn(fs)
        `when`(mapper.mapBeanCollection(mutableMapOf(), Phone::class.java)).thenReturn(listOf())
        `when`(mapper.superMapFieldSet(any())).thenReturn(User())

        `when`(mapper.mapFieldSet(any())).thenCallRealMethod()

        val user = mapper.mapFieldSet(fs)

        verify(mapper).extractExpandoAttributes(any())
        verify(mapper).extractAllBeanCollectionFields(any())
        verify(mapper).removeFields(any(), any())
        verify(mapper).superMapFieldSet(any())
        verify(mapper).mapBeanCollection(any(), eq(Phone::class.java))

    }

    fun <T>eq(value: T): T  {
        var result = ArgumentMatchers.eq(value)
        if(result == null) {
            result = value
        }
        return result
    }

    @Test
    fun testExtractAllBeanCollectionFields () {

        val names = arrayOf(
                "phone[0].extension", "phone[0].number",
                "phone[1].extension", "phone[1].number",
                "phone[3].extension", "phone[3].number",
                "anyComposite[anyKey].someField",
                "emailAddress",
                "screenName")

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
    open fun testMapBeanCollection_empty_fields () {

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
    open fun testRemoveDotNotationFields () {

        val fs = DefaultFieldSet(
                arrayOf("elton","0102030405","j"),
                arrayOf("screenName", "phone[0].number","lastName"))

        val newfs = mapper.removeFields(fs, { name, value -> name.contains('.') })

        assertArrayEquals(arrayOf("screenName","lastName"), newfs!!.names)
        assertArrayEquals(arrayOf("elton","j"), newfs!!.values)

    }

    @Test
    open fun testRemoveDotNotationFields_no_dot_notation_field () {

        val fs = DefaultFieldSet(
                arrayOf("elton","j"),
                arrayOf("screenName","lastName"))

        val newfs = mapper.removeFields(fs, { name, value -> name.contains('.') })

        assertArrayEquals(fs.names, newfs!!.names)
        assertArrayEquals(fs.values, newfs!!.values)

    }
}