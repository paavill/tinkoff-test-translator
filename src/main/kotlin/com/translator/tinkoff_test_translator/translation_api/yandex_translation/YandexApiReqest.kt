package com.translator.tinkoff_test_translator.translation_api.yandex_translation

import com.translator.tinkoff_test_translator.translation_api.RequestToApi

data class YandexApiReqest(
    val sourceLanguageCode: String,
    val targetLanguageCode: String,
    val texts: List<String>
) : RequestToApi