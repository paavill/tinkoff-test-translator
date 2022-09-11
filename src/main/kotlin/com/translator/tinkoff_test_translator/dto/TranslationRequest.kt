package com.translator.tinkoff_test_translator.dto

data class TranslationRequest(
    val originalLanguage: String,
    val targetLanguage: String,
    val translatedString: String
)