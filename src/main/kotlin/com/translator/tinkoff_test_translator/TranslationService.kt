package com.translator.tinkoff_test_translator

import com.translator.tinkoff_test_translator.dto.DataForTranslation
import com.translator.tinkoff_test_translator.dto.TranslatedData
import com.translator.tinkoff_test_translator.yandex_translation.YandexTranslationService
import org.springframework.stereotype.Service

@Service
class TranslationService(private val yandexTranslationService: YandexTranslationService) {
    fun translateByYandex(dataForTranslation: DataForTranslation): TranslatedData {
        return yandexTranslationService.translate(dataForTranslation)
    }
}