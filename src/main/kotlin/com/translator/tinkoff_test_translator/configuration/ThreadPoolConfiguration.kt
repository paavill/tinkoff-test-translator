package com.translator.tinkoff_test_translator.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
class ThreadPoolConfiguration(
    @Value("\${thread_pool.thread_number}")
    private val threadPoolThreadNumber: Int
) {
    @Bean
    fun threadPool(): ExecutorService {
        return Executors.newFixedThreadPool(threadPoolThreadNumber)
    }
}