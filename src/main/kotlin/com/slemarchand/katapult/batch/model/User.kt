package com.slemarchand.katapult.batch.model

import java.util.*

class User {
        var companyId: Long = 0
        var userId: Long = 0
        val autoPassword = false
        var screenName: String? = null
        var emailAddress: String? = null
        var password: String? = null
        var firstName: String? = null
        var lastName: String? = null
        var middleName: String? = ""
        var jobTitle: String? = ""
        var passwordReset = false
        var agreedToTermsOfUse = true
        var portraitBytes: Array<Byte>? = null
        var facebookId: Long = 0
        var openId: String? = null
        var languageId: String = "en_US"
        var timeZoneId: String? = null
        var greeting: String? = null
        var comments: String? = null
        val contact = Contact()
        var phones: List<Phone> = LinkedList()
        var expandoBridgeAttributes: Map<String, String> = HashMap<String, String>()
}
