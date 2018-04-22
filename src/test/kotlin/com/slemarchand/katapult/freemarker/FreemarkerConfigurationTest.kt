package com.slemarchand.katapult.freemarker;

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class FreemarkerConfigurationTest {

    @Test
    fun testConfiguration() {
        try {
            val cfg = FreemarkerConfiguration().configuration()
        } catch (e: Exception) {
            fail<Any>(e)
        }
    }
}
