package com.translator.tinkoff_test_translator.controller

import com.translator.tinkoff_test_translator.exception.TranslateServiceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackageClasses = [TranslatorController::class])
class TranslationControllerAdvice {
    @ExceptionHandler(TranslateServiceException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleTranslateServiceException(exception: TranslateServiceException): ResponseEntity<ExceptionResponseBody> {
        return ResponseEntity(
            ExceptionResponseBody(exception.service, exception.info),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}

data class ExceptionResponseBody(
    val service: String,
    val info: String
)