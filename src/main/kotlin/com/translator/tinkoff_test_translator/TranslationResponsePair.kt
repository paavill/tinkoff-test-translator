package com.translator.tinkoff_test_translator

import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity

data class TranslationResponsePair<T, Q>(
    val request: HttpEntity<T>,
    val response: ResponseEntity<Q>
)