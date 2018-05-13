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
import org.springframework.util.MultiValueMap
import org.springframework.util.LinkedMultiValueMap



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


    fun invoke(json: String): String? {
        return invoke(json, String::class.java)
    }

    fun <T>invoke(json: String, responseClass: Class<T>): T?{

        val headers = HttpHeaders()

        headers.contentType = MediaType.APPLICATION_JSON

        val user = parameters.user!!

        val password = parameters.password!!

        val companyId = parameters.companyId!!

        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("${user}:${password}".toByteArray(Charset.defaultCharset())))

        headers.set("Cookie","COMPANY_ID=${companyId}")

        val url = "${KatapultApplication.parameters.server}/api/jsonws/invoke"


        var response: T? = null

            val wwwForm = false

        var entity: Any?

        if(wwwForm) {

            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            val data = LinkedMultiValueMap<String, String>()

            data.add("cmd", json)

            entity = HttpEntity<MultiValueMap<String, String>>(data, headers)


        } else {

            entity = HttpEntity(json, headers)

        }

        try {

            response = restTemplate.postForObject(url, entity, responseClass)

        } catch (e: HttpServerErrorException) {
            log.error(e.responseBodyAsString)

            throw Exception(e)
        }

        return response
    }

    companion object {

        private val log = LoggerFactory.getLogger(UserItemWriter::class.java)
    }
}