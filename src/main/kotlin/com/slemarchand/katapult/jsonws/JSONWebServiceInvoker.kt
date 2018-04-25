package com.slemarchand.katapult.jsonws

import com.slemarchand.katapult.KatapultApplication
import com.slemarchand.katapult.Parameters
import com.slemarchand.katapult.batch.UserItemWriter
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import java.nio.charset.Charset
import java.util.*

@Component
open class JSONWebServiceInvoker {

    private val restTemplate: RestTemplate

    private val parameters: Parameters

    constructor(restTemplate: RestTemplate) {

        this.restTemplate  = restTemplate

        this.parameters = KatapultApplication.parameters

        if(parameters.insecure) {
            SSLUtil.acceptInsecureConnections()
        }
    }

    fun invoke(json: String) {

        val headers = HttpHeaders()

        headers.contentType = MediaType.APPLICATION_JSON

        val user = parameters.user!!

        val password = parameters.password!!

        val companyId = parameters.companyId!!

        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("${user}:${password}".toByteArray(Charset.defaultCharset())))

        headers.set("Cookie","COMPANY_ID=${companyId}")

        val url = "${KatapultApplication.parameters.server}/api/jsonws/invoke"

        val entity = HttpEntity(json, headers)

        var response: String? = null

        try {
            response = restTemplate.postForObject(url, entity, String::class.java)

        } catch (e: HttpServerErrorException) {
            log.error(e.responseBodyAsString)
        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(UserItemWriter::class.java)
    }
}