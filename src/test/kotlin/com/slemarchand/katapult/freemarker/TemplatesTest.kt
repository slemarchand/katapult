package com.slemarchand.katapult.freemarker

import com.fasterxml.jackson.core.JsonParseException
import com.slemarchand.katapult.batch.UserFieldSetMapper
import com.slemarchand.katapult.batch.model.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.jackson.JsonObjectDeserializer
import java.io.StringWriter
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper



class TemplatesTest {


    val cfg = FreemarkerConfiguration().configuration()


    @Test
    fun testCreateMinimalData() {

        val user = User()

        user.screenName = "anyScreenName"

        val output = processTemplate(Templates.CREATE, user)

        try {
            deserialize(output)
        } catch( e: JsonParseException) {
            Assertions.fail<Any>(e)
        }
    }

    @Test
    fun testUpdateMinimalData() {

        val user = User()

        user.screenName = "anyScreenName"

        val output = processTemplate(Templates.UPDATE, user)

        try {
            deserialize(output)
        } catch( e: JsonParseException) {
            Assertions.fail<Any>(e)
        }
    }


    fun deserialize(jsonString: String): JsonNode {

        val mapper = ObjectMapper()

        val rootNode = mapper.readTree(jsonString)

        return rootNode
    }

    fun processTemplate(templateName: String, user: User): String {

        val template = cfg.getTemplate(templateName)

        val buffer = StringWriter()

        template?.process(user, buffer)

        return buffer.toString()
    }

}
