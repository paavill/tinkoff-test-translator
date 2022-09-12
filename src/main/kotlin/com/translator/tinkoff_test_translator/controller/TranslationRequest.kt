package com.translator.tinkoff_test_translator.controller

import com.translator.tinkoff_test_translator.controller.validation.LangIso639p1App

data class TranslationRequest(
    @field:LangIso639p1App
    val originalLanguage: String,
    @field:LangIso639p1App
    val targetLanguage: String,
    val translatedString: String
)