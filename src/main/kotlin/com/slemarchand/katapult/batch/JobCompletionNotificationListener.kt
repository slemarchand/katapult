package com.slemarchand.katapult.batch

import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JobCompletionNotificationListener

    @Autowired
    constructor() : JobExecutionListenerSupport() {

    override fun afterJob(jobExecution: JobExecution?) {
        if (jobExecution!!.status == BatchStatus.COMPLETED) {
            log.info("JOB FINISHED!")
        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(JobCompletionNotificationListener::class.java)
    }
}
