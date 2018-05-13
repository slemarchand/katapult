package com.slemarchand.katapult.batch.model

import java.util.*


class Contact {

	var prefixId: Long = 0
	var suffixId: Long = 0
	var male = false
    var birthday: Date = Date(0)
	var smsSn: String? = null
    var facebookSn: String? = null
    var jabberSn: String? = null
    var skypeSn: String? = null
    var twitterSn: String? = null
}