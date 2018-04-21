package com.slemarchand.katapult.batch

import com.slemarchand.katapult.Parameters
import org.junit.jupiter.api.Test

class BatchConfigurationTest {

    @Test
    fun testReader() {
        val parameters: Parameters = Parameters()
        parameters.path = "/some/path"
        val batchConfiguration = object: BatchConfiguration() {

            override fun names(): Array<String> {
                return arrayOf("someField")
            }
        }
        var reader = batchConfiguration.reader(parameters)
    }
}