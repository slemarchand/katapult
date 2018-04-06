package com.slemarchand.katapult.batch

import com.slemarchand.katapult.batch.model.User
import org.slf4j.LoggerFactory
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet
import org.springframework.validation.DataBinder

open class UserFieldSetMapper:  BeanWrapperFieldSetMapper<User>(){

    init {
        setTargetType(User::class.java)
        setStrict(false)
    }

    override fun createBinder(target: Any?): DataBinder {
        return super.createBinder(target)
    }

    override fun mapFieldSet(fs: FieldSet?): User {
        
        val expandoAttributes = extractExpandoAttributes(fs);

        val user = super.mapFieldSet(fs)

        user.expandoBridgeAttributes = expandoAttributes

        return user;
    }

    protected fun extractExpandoAttributes(fs: FieldSet?): Map<String, String> {

        val attrs = HashMap<String, String>()

        fs!!.properties.forEach({

            val key = it.key.toString()

            if(key.startsWith("expando:")) {

                val expandoName = key.substring("expando:".length, key.length)

                attrs.put(expandoName, it.value.toString())
            }
        })

        return attrs
    }

    companion object {

        private val log = LoggerFactory.getLogger(UserFieldSetMapper::class.java)
    }
}