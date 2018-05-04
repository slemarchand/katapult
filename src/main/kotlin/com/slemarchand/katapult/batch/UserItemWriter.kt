package com.slemarchand.katapult.batch

import com.slemarchand.katapult.KatapultApplication
import com.slemarchand.katapult.Parameters
import com.slemarchand.katapult.jsonws.JSONWebServiceInvoker
import com.slemarchand.katapult.batch.model.User
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

            val templateKey = if(parameters.updateMode) "update" else "User#addUser"

            val template =  freemarkerConfiguration.getTemplate("/jsonws/${templateKey}.json.ftl")

            val buffer = StringWriter()

            template?.process(it, buffer)

            val requestJson = buffer.toString()

            invoker.invoke(requestJson)
        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(UserItemWriter::class.java)
    }
}