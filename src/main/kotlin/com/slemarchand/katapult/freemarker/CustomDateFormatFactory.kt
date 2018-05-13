package com.slemarchand.katapult.freemarker

import freemarker.core.*
import freemarker.template.TemplateDateModel
import java.text.SimpleDateFormat
import java.util.*
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalField

open class CustomDateFormatFactory : TemplateDateFormatFactory {

    private val templateDateformat: TemplateDateFormat

    constructor(format: (LocalDate) -> String): super() {

        this.templateDateformat = CustomDateFormat(format)
    }

    @Throws(TemplateValueFormatException::class)
    override fun get(
            params: String,
            dateType: Int, locale: Locale, timeZone: TimeZone, zonelessInput: Boolean,
            env: Environment): TemplateDateFormat {
            return templateDateformat
    }
}

class CustomDateFormat : TemplateDateFormat {

    private val format: (LocalDate) -> String

    constructor(format: (LocalDate) -> String): super() {

        this.format = format
    }

    override fun formatToPlainText(dateModel: TemplateDateModel?): String {

        var value: String = ""

        if(dateModel != null) {
            val localDate = dateModel.asDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

            value = format(localDate)
        }

        return value
    }

    override fun isLocaleBound(): Boolean {
        return false
    }

    override fun parse(s: String?, dateType: Int): Any {
            throw UnsupportedOperationException()
    }

    override fun getDescription(): String {
        return ""
    }

    override fun isTimeZoneBound(): Boolean {
        return false
    }
}

object DayDateFormatFactory : CustomDateFormatFactory({it.dayOfMonth.toString()})

object MonthDateFormatFactory : CustomDateFormatFactory({(it.monthValue - 1).toString()})

object YearDateFormatFactory : CustomDateFormatFactory({it.year.toString()})