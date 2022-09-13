package com.translator.tinkoff_test_translator.translation_api

import com.fasterxml.jackson.annotation.JsonProperty

data class YandexApiResponse(@JsonProperty("translations") val translations: List<Text>)

data class Text(@JsonProperty("text") val text: String)