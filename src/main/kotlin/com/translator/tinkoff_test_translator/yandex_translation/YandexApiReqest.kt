package com.translator.tinkoff_test_translator.yandex_translation

import com.translator.tinkoff_test_translator.RequestToApi

data class YandexApiReqest(
    val sourceLanguageCode: String,
    val targetLanguageCode: String,
    val texts: List<String>
) : RequestToApi