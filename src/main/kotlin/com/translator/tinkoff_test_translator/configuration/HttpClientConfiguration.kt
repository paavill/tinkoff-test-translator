package com.translator.tinkoff_test_translator.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class HttpClientConfiguration {
    @Bean
    fun httpClient() = RestTemplate()
}