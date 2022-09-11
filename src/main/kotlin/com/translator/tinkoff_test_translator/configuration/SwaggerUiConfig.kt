package com.translator.tinkoff_test_translator.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider
import springfox.documentation.swagger.web.SwaggerResource
import springfox.documentation.swagger.web.SwaggerResourcesProvider


@Configuration
class SwaggerUiConfig {
    @Primary
    @Bean
    fun swaggerResourcesProvider(defaultResourcesProvider: InMemorySwaggerResourcesProvider): SwaggerResourcesProvider {
        return SwaggerResourcesProvider {
            val wsResource = SwaggerResource()
            wsResource.name = "endpoints"
            wsResource.swaggerVersion = "3.0"
            wsResource.location = "/swagger-config.json"
            val resources: MutableList<SwaggerResource> = mutableListOf(wsResource)
            resources.addAll(defaultResourcesProvider.get())
            resources
        }
    }

    @Bean
    fun api(): Docket? {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.translator.tinkoff_test_translator")) // Пакет сканирования Swagger
            .paths(PathSelectors.any())
            .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("Translator")
            .contact(Contact("Pavel", "https://github.com/paavill", "pavelchist2001@gmail.com"))
            .description("Test task for passing the second stage of the interview at Tinkoff")
            .version("1.0")
            .build()
    }


}