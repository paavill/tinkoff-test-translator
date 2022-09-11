package com.translator.tinkoff_test_translator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
class TinkoffTestTranslatorApplication

fun main(args: Array<String>) {
    runApplication<TinkoffTestTranslatorApplication>(*args)
}
