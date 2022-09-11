package com.translator.tinkoff_test_translator.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
class ThreadPoolConfiguration {
    @Bean
    fun threadPool(): ExecutorService {
        return Executors.newFixedThreadPool(10)
    }
}