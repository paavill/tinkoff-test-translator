package com.translator.tinkoff_test_translator.translation_api.service

import com.translator.tinkoff_test_translator.dto.DataForTranslation
import com.translator.tinkoff_test_translator.dto.TranslatedData
import com.translator.tinkoff_test_translator.translation_api.yandex_translation.YandexTranslationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TranslationService(
    private val yandexTranslationService: YandexTranslationService,
    private val translationApiIndependentService: TranslationApiIndependentService
) {

    private val logger = LoggerFactory.getLogger(TranslationService::class.java)

    fun translateByYandex(dataForTranslation: DataForTranslation): TranslatedData {
        logger.info("Translation by Yandex API started.")
        return translationApiIndependentService.translateByAnyTranslator(dataForTranslation, yandexTranslationService)
    }

}