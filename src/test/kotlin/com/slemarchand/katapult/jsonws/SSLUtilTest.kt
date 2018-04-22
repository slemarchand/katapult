package com.slemarchand.katapult.jsonws

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection
import org.mockito.Mockito
import javax.net.ssl.SSLSession


class SSLUtilTest {

    @Test
    fun testAcceptInsecureConnections_yes_hostname_verifier() {

        SSLUtil.acceptInsecureConnections();

        val verifier = HttpsURLConnection.getDefaultHostnameVerifier()

        val verified = verifier.verify("ff.ff", Mockito.mock(SSLSession::class.java))

        assertTrue(verified)
    }
}