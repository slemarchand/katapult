package com.slemarchand.katapult

import com.beust.jcommander.Parameter

class Parameters {

    @Parameter
    var path: String? = null

    @Parameter(names = arrayOf("-u", "--user"),
            description = "The user")
    var user: String? = null

    @Parameter(names = arrayOf("-p", "--password"),
            description = "The password")
    var password: String? = null


    @Parameter(names = arrayOf("-s", "--server"),
            description = "The server base URL ")
    var server: String? = null

    @Parameter(names = arrayOf("-c", "--companyId"),
            description = "The company ID")
    var companyId: Long? = null
}