package com.slemarchand.katapult.batch

import com.slemarchand.katapult.*
import com.slemarchand.katapult.batch.model.User
import com.slemarchand.katapult.jsonws.JSONWebServiceInvoker
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.client.RestTemplate
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


@Configuration
@EnableBatchProcessing
open class BatchConfiguration {

    @Autowired
    var jobBuilderFactory: JobBuilderFactory? = null

    @Autowired
    var stepBuilderFactory: StepBuilderFactory? = null

    @Bean
    open fun reader(): ItemReader<User> {
        return reader(KatapultApplication.parameters)
    }

    fun reader(parameters: Parameters): ItemReader<User> {

        return FlatFileItemReaderBuilder<User>()
                .name("userItemReader")
                .resource(FileSystemResource(parameters.path))
                .linesToSkip(1)
                .delimited().names(names())
                .recordSeparatorPolicy(DefaultRecordSeparatorPolicy())
                .fieldSetMapper(UserFieldSetMapper()).build()
    }

    protected open fun names(): Array<String> {

        val file = File(KatapultApplication.parameters.path)

        val reader = BufferedReader(FileReader(file))

        val firstLine = reader.readLine()

        val names =  firstLine.split(",").toTypedArray()

        return names
    }

    @Bean
    open fun writer(invoker: JSONWebServiceInvoker, freemarkerConfiguration: freemarker.template.Configuration): UserItemWriter {
        return UserItemWriter(invoker, freemarkerConfiguration)
    }

    @Bean
    open fun importUserJob(listener: JobExecutionListener, step1: Step): Job {
        return jobBuilderFactory!!.get("importUserJob")
                .incrementer(RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build()
    }

    @Bean
    open fun step1(processor: UserItemProcessor, writer: UserItemWriter): Step {
        return stepBuilderFactory!!.get("step1")
                .chunk<User, User>(10)
                .reader(reader())
                .processor(processor)
                .writer(writer)
                .build()
    }

    @Bean
    open fun restTemplate(builder: RestTemplateBuilder): RestTemplate {

        val restTemplate = builder.build()

        return restTemplate;
    }
}
