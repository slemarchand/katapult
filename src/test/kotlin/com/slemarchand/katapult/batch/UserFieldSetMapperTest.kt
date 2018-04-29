package com.slemarchand.katapult.batch

import com.slemarchand.katapult.batch.model.Phone
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.batch.item.file.transform.DefaultFieldSet
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class UserFieldSetMapperTest {

    lateinit var mapper: UserFieldSetMapper

    @BeforeEach
    open fun init() {
        mapper = UserFieldSetMapper()
    }

    @Test
    open fun testExtractAllBeanCollectionFields () {

        init()

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

        init()

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
    open fun testRemoveDotNotationFields () {

        init()

        val fs = DefaultFieldSet(
                arrayOf("elton","0102030405","j"),
                arrayOf("screenName", "phone[0].number","lastName"))

        val newfs = mapper.removeDotNotationFields(fs)

        assertArrayEquals(arrayOf("screenName","lastName"), newfs!!.names)
        assertArrayEquals(arrayOf("elton","j"), newfs!!.values)

    }
}