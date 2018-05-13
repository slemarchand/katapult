package com.slemarchand.katapult.freemarker

import freemarker.core.TemplateDateFormatFactory
import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import org.springframework.context.annotation.Bean


@org.springframework.context.annotation.Configuration
open class FreemarkerConfiguration {

    @Bean
    open fun configuration(): Configuration {
        // Create your Configuration instance, and specify if up to what FreeMarker
        // version (here 2.3.27) do you want to apply the fixes that are not 100%
        // backward-compatible. See the Configuration JavaDoc for details.
        val cfg: Configuration = Configuration(Configuration.VERSION_2_3_27);

        // Specify the source where the template files come from. Here I set a
        // plain directory for it, but non-file-system sources are possible too:
        cfg.setClassForTemplateLoading(FreemarkerConfiguration::class.java, "/templates");

        // Set the preferred charset template files are stored in. UTF-8 is
        // a good choice in most applications:
        cfg.setDefaultEncoding("UTF-8");

        // Sets how errors will appear.
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Don't log exceptions inside FreeMarker that it will thrown at you anyway:
        cfg.setLogTemplateExceptions(false);

        // Wrap unchecked exceptions thrown during template processing into TemplateException-s.
        cfg.setWrapUncheckedExceptions(true);

        val customDateFormats = HashMap<String, TemplateDateFormatFactory>()

        customDateFormats.put("day", DayDateFormatFactory)

        customDateFormats.put("month", MonthDateFormatFactory)

        customDateFormats.put("year", YearDateFormatFactory)

        cfg.customDateFormats = customDateFormats

        return cfg
    }
}