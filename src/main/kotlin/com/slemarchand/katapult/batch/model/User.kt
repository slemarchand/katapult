package com.slemarchand.katapult.batch.model

class User {
        var companyId: Long = 0
        val autoPassword = false
        var screenName: String? = null
        var emailAddress: String? = null
        var password: String? = null
        var firstName: String? = null
        var lastName: String? = null
        var middleName: String? = ""
        var jobTitle: String? = ""
        var portraitBytes: Array<Byte>? = null
        var expandoBridgeAttributes: Map<String, String> = HashMap<String, String>()
}
