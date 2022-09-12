package com.translator.tinkoff_test_translator

import com.translator.tinkoff_test_translator.dto.DataForTranslation
import com.translator.tinkoff_test_translator.dto.TranslatedData
import com.translator.tinkoff_test_translator.yandex_translation.YandexTranslationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TranslationService(
    private val yandexTranslationService: YandexTranslationService,
    private val cachingService: CachingService,
    private val requestService: ThreadPoolRequestService,
// TODO: 12.09.2022  
) {

    private val logger = LoggerFactory.getLogger(TranslationService::class.java)

    fun translateByYandex(dataForTranslation: DataForTranslation): TranslatedData {
        logger.info("Translation by Yandex API started.")
        return translateByAnyTranslator(dataForTranslation, yandexTranslationService)
    }

    private fun translateByAnyTranslator(
        dataForTranslation: DataForTranslation, apiTranslationService: ApiTranslationService
    ): TranslatedData {
        val constraint = apiTranslationService.getConstraint()
        val translatedDataAccumulator = mutableMapOf<String, TranslatedPair>()
        val toApiTranslate = getDataForTranslationByApi(dataForTranslation, translatedDataAccumulator)
        val translatedByApi = toApiTranslate.words.chunked(constraint.numberOfWordsPerTime).map {
            val start = System.currentTimeMillis()
            val translatedPairs = requestService.translate(
                DataForTranslation(
                    dataForTranslation.originalLanguage, dataForTranslation.targetLanguage, it
                ),
                apiTranslationService
            )
            val end = System.currentTimeMillis()
            val delta = if (end - start >= constraint.time) 0 else constraint.time - (end - start)
            Thread.sleep(delta)
            translatedPairs
        }.flatMap { chunk -> chunk.map { pair -> pair.original to pair } }.toMap()
        translatedDataAccumulator.putAll(translatedByApi)
        val result = dataForTranslation.words.map { original ->
            translatedDataAccumulator[original]!!.translated
        }
        logger.info(String.format("Translated %d unique words; %d by API.", translatedDataAccumulator.keys.size, toApiTranslate.words.size))
        return TranslatedData(
            result, translatedDataAccumulator.filter { toApiTranslate.words.contains(it.key) }.values.toList()
        )
    }

    private fun getDataForTranslationByApi(
        dataForTranslation: DataForTranslation, translatedDataAccumulator: MutableMap<String, TranslatedPair>
    ): DataForTranslation {
        val originalWordTranslatedPairMap =
            cachingService.getCachedKeyValueOriginalWordAndTranslatedPairs(dataForTranslation)
        translatedDataAccumulator.putAll(originalWordTranslatedPairMap)
        val toApiTranslate =
            dataForTranslation.words.toSet().filter { !originalWordTranslatedPairMap.keys.contains(it) }
        return DataForTranslation(
            dataForTranslation.originalLanguage, dataForTranslation.targetLanguage, toApiTranslate
        )
    }
}