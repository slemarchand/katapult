package com.slemarchand.katapult.freemarker

import freemarker.core.Environment
import freemarker.core.TemplateDateFormat
import freemarker.ext.beans.DateModel
import java.time.LocalDate
import java.util.*
import freemarker.cache.StringTemplateLoader
import freemarker.ext.beans.BeansWrapper
import freemarker.ext.beans.BeansWrapperBuilder
import freemarker.template.Version
import org.junit.jupiter.api.*
import java.time.ZoneId

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomDateFormatFactoryTest {

    lateinit var env: Environment

    lateinit var wrapper: BeansWrapper

    lateinit var dayFormat: TemplateDateFormat

    lateinit var monthFormat: TemplateDateFormat

    lateinit var yearFormat: TemplateDateFormat


    @BeforeAll
    fun initAll() {

        val cfg = FreemarkerConfiguration().configuration()

        val stl = StringTemplateLoader()

        stl.putTemplate("someTemplate", "")

        cfg.templateLoader = stl

        env = Environment(cfg.getTemplate("someTemplate"),  null, null)

        wrapper = BeansWrapperBuilder(Version("2.3.0")).build()

    }

    @BeforeEach
    fun initEach() {


        val locale = Locale.getDefault()

        val timeZone = TimeZone.getDefault()

        dayFormat = DayDateFormatFactory.get("", 0, locale, timeZone, false, env)

        monthFormat = MonthDateFormatFactory.get("", 0, locale, timeZone, false, env)

        yearFormat = YearDateFormatFactory.get("", 0, locale, timeZone, false, env)
    }

    @Test
    fun testFormatToPlainText() {

        // December 1st, 1992

        val localDate = LocalDate.of(1992,12, 31)

        val dateModel: DateModel = DateModel(Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), wrapper)

        println(dateModel)

        val day = dayFormat.formatToPlainText(dateModel)

        val month = monthFormat.formatToPlainText(dateModel)

        val year = yearFormat.formatToPlainText(dateModel)

        Assertions.assertEquals("31", day)
        Assertions.assertEquals("11", month)
        Assertions.assertEquals("1992", year)
    }

}