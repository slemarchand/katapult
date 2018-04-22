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
}