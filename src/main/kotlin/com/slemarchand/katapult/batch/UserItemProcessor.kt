package com.slemarchand.katapult.batch

import com.slemarchand.katapult.Application
import com.slemarchand.katapult.batch.model.User
import org.slf4j.LoggerFactory

import org.springframework.batch.item.ItemProcessor

class UserItemProcessor : ItemProcessor<User, User> {

    @Throws(Exception::class)
    override fun process(person: User): User {

        person.companyId = Application.parameters.companyId!!

        return person
    }

    companion object {

        private val log = LoggerFactory.getLogger(UserItemProcessor::class.java)
    }

}
