package com.translator.tinkoff_test_translator.yandex_translation

import com.fasterxml.jackson.annotation.JsonProperty

data class YandexApiResponse(@JsonProperty("translations") val translations: List<Word>)

data class Word(@JsonProperty("text") val text: String)