package com.slemarchand.katapult

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import com.beust.jcommander.JCommander
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration

@SpringBootApplication
open class Application {

    companion object CommandLine {

        val parameters: Parameters = Parameters()

        fun parseArgs(args: Array<String>) {

            JCommander.newBuilder()
                    .addObject(parameters)
                    .build()
                    .parse(*args);
        }
    }
}

fun main(args: Array<String>) {

    Application.parseArgs(args);

    SpringApplication.run(Application::class.java)
}