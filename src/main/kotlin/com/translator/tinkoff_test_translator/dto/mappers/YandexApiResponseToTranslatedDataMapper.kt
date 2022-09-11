package com.translator.tinkoff_test_translator.dto.mappers

import com.translator.tinkoff_test_translator.yandex_translation.YandexApiResponse
import com.translator.tinkoff_test_translator.dto.TranslatedData
import org.springframework.stereotype.Component

@Component
class YandexApiResponseToTranslatedDataMapper {
    fun map(yandexApiResponse: YandexApiResponse) : TranslatedData {
        val words = yandexApiResponse.translations.map { word -> word.text }.toList()
        return TranslatedData(words)
    }
}