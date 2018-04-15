package com.slemarchand.katapult

import com.beust.jcommander.Parameter

class Parameters {

    @Parameter(required = true,
            description = "<csv-file>")
    var path: String? = null

    @Parameter(names = arrayOf("-u", "--user"),
            required = true,
            order = 30,
            description = "The user")
    var user: String? = null

    @Parameter(names = arrayOf("-p", "--password"),
            required = true,
            order = 31,
            description = "The password")
    var password: String? = null

    @Parameter(names = arrayOf("-s", "--server"),
            required = true,
            order = 10,
            description = "The server base URL ")
    var server: String? = null

    @Parameter(names = arrayOf("-c", "--companyId"),
            required = true,
            order = 20,
            description = "The company ID")
    var companyId: Long? = null

    @Parameter(names = arrayOf("-k", "--insecure"),
            description = "(TLS) By default, every SSL connection katapult makes is verified to be secure. " +
                "This option allows katapult to proceed and operate even for server connections " +
                "otherwise considered insecure")
    var insecure: Boolean = false
}