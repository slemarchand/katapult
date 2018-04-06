
import java.security.NoSuchAlgorithmException
import java.security.KeyManagementException
import java.rmi.server.RMISocketFactory.getSocketFactory
import java.security.cert.CertificateException
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.X509Certificate

class SSLUtil private constructor() {

    companion object {

        private val UNQUESTIONING_TRUST_MANAGER = arrayOf<TrustManager>(object : X509TrustManager {

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate>? = null

            @Throws(CertificateException::class)
            override fun checkClientTrusted(var1: Array<java.security.cert.X509Certificate>, var2: String) {}

            @Throws(CertificateException::class)
            override fun checkServerTrusted(var1: Array<java.security.cert.X509Certificate>, var2: String) {}
        })

        @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
        fun acceptInsecureConnections() {


            HttpsURLConnection.setDefaultHostnameVerifier { hostname, sslSession ->
                true
            }

            // Install the all-trusting trust manager
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, UNQUESTIONING_TRUST_MANAGER, null)
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
        }
    }
}