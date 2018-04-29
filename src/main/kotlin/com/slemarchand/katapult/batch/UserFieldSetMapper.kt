package com.slemarchand.katapult.batch

import com.slemarchand.katapult.batch.model.Phone
import com.slemarchand.katapult.batch.model.User
import org.slf4j.LoggerFactory
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.transform.DefaultFieldSet
import org.springframework.batch.item.file.transform.FieldSet
import org.springframework.validation.DataBinder
import java.util.*
import kotlin.reflect.KClass

open class UserFieldSetMapper:  BeanWrapperFieldSetMapper<User> {

val mappersByClass : MutableMap<Class<out Any>, BeanWrapperFieldSetMapper<out Any>> = HashMap()

    constructor() {
        setTargetType(User::class.java)
        setStrict(false)
    }

    override fun createBinder(target: Any?): DataBinder {
        return super.createBinder(target)
    }

    override fun mapFieldSet(fs: FieldSet?): User {

        val expandoAttributes = extractExpandoAttributes(fs);

        val compositeCollectionFields  = extractAllBeanCollectionFields(fs);

        val simpleFieldSet = removeDotNotationFields(fs)

        val user = super.mapFieldSet(simpleFieldSet)

        user.phones = mapBeanCollection(compositeCollectionFields.get("phones"), Phone::class.java)

        user.expandoBridgeAttributes = expandoAttributes

        return user;
    }

    fun removeDotNotationFields(fs: FieldSet?): FieldSet? {

        val tokens = fs!!.values

        val names = fs!!.names

        val newTokens = LinkedList<String>()

        val newNames = LinkedList<String>()

        for(i in 0.. names.size - 1) {

            val name = names[i]

            if(!name.contains('.')) {
                newNames.add(name)
                newTokens.add(tokens[i])
            }
        }

        var result: FieldSet? = null

        if(names.size == newNames.size) {
            result = fs
        } else {
            result = DefaultFieldSet(
                    newTokens.toTypedArray(),
                    newNames.toTypedArray())
        }

        return result
    }

    fun extractExpandoAttributes(fs: FieldSet?): Map<String, String> {
        
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

    fun extractAllBeanCollectionFields(fs: FieldSet?): MutableMap<String, MutableMap<String, MutableMap<String, String>>> {

        val result = HashMap<String, MutableMap<String, MutableMap<String, String>>>()

        fs!!.properties.forEach({

            val key = it.key.toString()

            val openingBracketIndex = key.indexOf('[')

            if(openingBracketIndex != -1) {

                val closingBracketIndex = key.indexOf(']')

                val typeName = key.substring(0, openingBracketIndex)

                val instanceName = key.substring(openingBracketIndex + 1, closingBracketIndex)

                val fieldName = key.substring(closingBracketIndex + 2)

                val value = it.value

                var typeMap = result.get(typeName)

                if(typeMap == null) {
                    typeMap = HashMap<String, MutableMap<String, String>>()
                    result.put(typeName, typeMap)
                }

                var instanceMap = typeMap.get(instanceName)

                if(instanceMap == null) {
                    instanceMap = HashMap()
                    typeMap.put(instanceName, instanceMap)
                }

                instanceMap.put(fieldName, value.toString())
            }
        })

        return result
    }

    fun <T>mapBeanCollection(data: MutableMap<String, MutableMap<String, String>> ?, clazz: Class<T>) : List<T> {

        val result = LinkedList<T>()

        if (data != null) {

            val keys = LinkedList(data!!.keys)

            keys.sort()

            for (it in keys) {

                val properties = data!!.get(it)!!

                if (properties.values.filter { it != null && it.trim().isNotEmpty() }.isEmpty()) {
                    continue
                }

                val tokens: Array<String?> = arrayOfNulls(properties.size)

                val names: Array<String?> = arrayOfNulls(properties.size)

                var i = 0

                properties.entries.forEach {
                    names[i] = it.key
                    tokens[i] = it.value
                    i++
                }

                val fieldSet = DefaultFieldSet(tokens, names)

                var mapper = mappersByClass.get(clazz)

                if (mapper == null) {
                    mapper = BeanWrapperFieldSetMapper<Any>()
                    mapper.setTargetType(clazz)
                    mappersByClass.put(clazz as Class<Any>, mapper)
                }

                val bean = mapper!!.mapFieldSet(fieldSet)

                result.add(bean as T)
            }
        }

        return result
    }

    companion object {

        private val log = LoggerFactory.getLogger(UserFieldSetMapper::class.java)
    }
}

