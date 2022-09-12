package com.translator.tinkoff_test_translator.translation_api.service

import com.translator.tinkoff_test_translator.dto.DataForTranslation
import com.translator.tinkoff_test_translator.dto.TranslatedData
import com.translator.tinkoff_test_translator.translation_api.ApiTranslationService
import com.translator.tinkoff_test_translator.translation_api.model.TranslatedPair
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TranslationApiIndependentService(
    private val cachingService: CachingService,
    private val requestService: ThreadPoolRequestService,
) {
    private val logger = LoggerFactory.getLogger(TranslationApiIndependentService::class.java)

    fun translateByAnyTranslator(
        dataForTranslation: DataForTranslation, apiTranslationService: ApiTranslationService
    ): TranslatedData {
        val cachedTranslatedData = cachingService.getCachedKeyValueOriginalWordAndTranslatedPairs(dataForTranslation)
        val uniqueTranslatedData = getUniqueDataForTranslation(dataForTranslation)
        val toApiTranslate = getDataForTranslationByApi(uniqueTranslatedData, cachedTranslatedData)
        val translatedByApi = getTranslatedPairsByApi(toApiTranslate, apiTranslationService)
        val allTranslatedData = cachedTranslatedData + translatedByApi

        val translatedWords = dataForTranslation.words.map { original ->
            allTranslatedData[original]!!.translated
        }
        log(toApiTranslate, allTranslatedData)
        return TranslatedData(
            translatedWords, translatedByApi.values.toList()
        )
    }

    private fun getTranslatedPairsByApi(
        toApiTranslate: DataForTranslation, apiTranslationService: ApiTranslationService
    ): Map<String, TranslatedPair> {
        return requestService.translate(
            toApiTranslate, apiTranslationService
        ).map { it.original to it }.toMap()
    }

    private fun getUniqueDataForTranslation(dataForTranslation: DataForTranslation): DataForTranslation {
        return DataForTranslation(
            dataForTranslation.originalLanguage,
            dataForTranslation.targetLanguage,
            dataForTranslation.words.toSet().toList()
        )
    }

    private fun getDataForTranslationByApi(
        dataForTranslation: DataForTranslation, translatedDataAccumulator: Map<String, TranslatedPair>
    ): DataForTranslation {
        val toApiTranslate = dataForTranslation.words.toSet().filter { !translatedDataAccumulator.keys.contains(it) }
        return DataForTranslation(
            dataForTranslation.originalLanguage, dataForTranslation.targetLanguage, toApiTranslate
        )
    }

    private fun log(toApiTranslate: DataForTranslation, translatedDataAccumulator: Map<String, TranslatedPair>) {
        logger.info(
            String.format(
                "Translated %d unique words; %d by API.", translatedDataAccumulator.keys.size, toApiTranslate.words.size
            )
        )
    }
}