package com.translator.tinkoff_test_translator.dto

data class DataForTranslation(
    val originalLanguage: String,
    val targetLanguage: String,
    val words: List<String>
) 