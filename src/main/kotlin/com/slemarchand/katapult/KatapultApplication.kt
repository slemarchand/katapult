package com.slemarchand.katapult

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import org.springframework.boot.Banner
import kotlin.system.exitProcess

@SpringBootApplication
open class KatapultApplication {

    companion object CommandLine {

        val parameters: Parameters = Parameters()

        fun parseArgs(args: Array<String>) {

            val jCommander = JCommander.newBuilder()
                    .addObject(parameters)
                    .build()

            jCommander.setProgramName("katapult")
            jCommander.parse(*args);
        }
    }
}

fun main(args: Array<String>) {

    try {
        KatapultApplication.parseArgs(args);

        val app = SpringApplication(KatapultApplication::class.java)
        app.setLogStartupInfo(false)
        app.setBannerMode(Banner.Mode.OFF)
        app.run()
    } catch (e: ParameterException) {
        println(e.usage())
        exitProcess(1)
    }
}