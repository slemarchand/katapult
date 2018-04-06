package com.slemarchand.katapult.batch

import com.slemarchand.katapult.jsonws.JSONWebServiceInvoker
import com.slemarchand.katapult.batch.model.User
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter;
import freemarker.template.Configuration
import java.io.StringWriter


class UserItemWriter : ItemWriter<User>  {

    var invoker: JSONWebServiceInvoker

    var freemarkerConfiguration: Configuration

    constructor(invoker: JSONWebServiceInvoker, freemarkerConfiguration: Configuration) {
        this.invoker  = invoker
        this.freemarkerConfiguration = freemarkerConfiguration
    }

    override fun write(users: MutableList<out User>?) {

        users!!.forEach {

            log.info("Processing ${it.screenName} - ${it.emailAddress} - ${it.firstName} ${it.lastName}")

            val template =  freemarkerConfiguration.getTemplate("/jsonws/User#addUser.json.ftl")

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