package com.slemarchand.katapult.batch

import com.slemarchand.katapult.KatapultApplication
import com.slemarchand.katapult.Parameters
import com.slemarchand.katapult.jsonws.JSONWebServiceInvoker
import com.slemarchand.katapult.batch.model.User
import com.slemarchand.katapult.freemarker.Templates
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter;
import freemarker.template.Configuration
import java.io.StringWriter


class UserItemWriter : ItemWriter<User>  {

    val invoker: JSONWebServiceInvoker

    val freemarkerConfiguration: Configuration

    val parameters: Parameters

    constructor(invoker: JSONWebServiceInvoker, freemarkerConfiguration: Configuration, parameters: Parameters) {
        this.invoker  = invoker
        this.freemarkerConfiguration = freemarkerConfiguration
        this.parameters = parameters
    }

    override fun write(users: MutableList<out User>?) {

        users!!.forEach {

            log.info("Processing ${it.screenName} - ${it.emailAddress} - ${it.firstName} ${it.lastName}")


            if(parameters.updateMode) {

                val user = invoke(Templates.READ, it, Array<User>::class.java)

                invoke(Templates.UPDATE, user!![0], String::class.java)

            } else {

                invoke(Templates.CREATE, it, String::class.java)
            }
        }
    }

    private fun <T>invoke(templateName: String, user: User, responseClass: Class<T> ): T? {

        val template = freemarkerConfiguration.getTemplate(templateName)

        val buffer = StringWriter()

        template?.process(user, buffer)

        val requestJson = buffer.toString()

        return invoker.invoke(requestJson, responseClass)
    }

    companion object {

        private val log = LoggerFactory.getLogger(UserItemWriter::class.java)
    }
}