package com.slemarchand.katapult.portrait

import com.slemarchand.katapult.Application
import org.springframework.stereotype.Component
import java.io.File

@Component
class PortraitsRepository {

    val portraitsDirectory : File

    val available: Boolean

    constructor() {
        portraitsDirectory = File(File(Application.parameters.path).parentFile, "portraits")
        available = portraitsDirectory.exists() || portraitsDirectory.isDirectory()
    }

    fun getPortraitBytes(basename: String): Array<Byte>? {

        var bytes: Array<Byte>? = null

        if(available) {
            var portraitFile = File(portraitsDirectory, "${basename}.jpg")
            if(!portraitFile.exists() || !portraitFile.isFile()) {
                portraitFile = File(portraitsDirectory, "${basename}.jpeg")
            }

            if(portraitFile.exists() && portraitFile.isFile()) {
                bytes = portraitFile.readBytes().toTypedArray()
            }
        }

        return bytes
    }
}