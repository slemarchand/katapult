package com.slemarchand.katapult.batch

import com.beust.jcommander.ParameterException
import com.slemarchand.katapult.KatapultApplication
import com.slemarchand.katapult.Parameters
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class KatapultApplicationTest {

    @Test
    fun testParseArgs_empty() {

        val args = emptyArray<String>()

        try {

            KatapultApplication.CommandLine.parseArgs(args)

            fail<Any>("ParameterException must be thrown")

        } catch (e: ParameterException) {}
    }

    @Test
    fun testParseArgs_unknown_parameter() {

        val args = arrayOf("/some/path","--unknown-parameter")

        try {

            KatapultApplication.CommandLine.parseArgs(args)

            fail<Any>("ParameterException must be thrown")

        } catch (e: ParameterException) {}
    }

    @Test
    fun testParseArgs_valid_parameters() {

        val args = arrayOf("/some/path",
                "-c","100034",
                "-s","http://someserver.com",
                "-u","user",
                "-p","pass")

        try {

            KatapultApplication.CommandLine.parseArgs(args)

        } catch (e: ParameterException) {
            fail<Any>(e)
        }

        val params = KatapultApplication.parameters

        assertEquals("/some/path", params.path)
        assertEquals(100034L, params.companyId)
        assertEquals("http://someserver.com", params.server)
        assertEquals("user", params.user)
        assertEquals("pass", params.password)
    }
}