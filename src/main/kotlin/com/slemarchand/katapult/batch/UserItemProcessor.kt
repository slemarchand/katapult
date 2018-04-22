package com.slemarchand.katapult.batch

import com.slemarchand.katapult.KatapultApplication
import com.slemarchand.katapult.batch.model.User
import com.slemarchand.katapult.portrait.PortraitsRepository
import org.slf4j.LoggerFactory

import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
class UserItemProcessor : ItemProcessor<User, User> {

    val portraitsRepository: PortraitsRepository

    constructor(portraitsRepository: PortraitsRepository) {
        this.portraitsRepository = portraitsRepository
    }

    @Throws(Exception::class)
    override fun process(user: User): User {

        user.companyId = KatapultApplication.parameters.companyId!!

        user.portraitBytes = portraitsRepository.getPortraitBytes(user.screenName!!)

        return user
    }

    companion object {

        private val log = LoggerFactory.getLogger(UserItemProcessor::class.java)
    }
}
