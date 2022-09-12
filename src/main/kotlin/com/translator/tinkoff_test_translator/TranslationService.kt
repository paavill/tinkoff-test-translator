package com.translator.tinkoff_test_translator

import com.translator.tinkoff_test_translator.dto.DataForTranslation
import com.translator.tinkoff_test_translator.dto.TranslatedData
import com.translator.tinkoff_test_translator.yandex_translation.YandexTranslationService
import org.springframework.stereotype.Service
import java.util.Collections.synchronizedMap
import java.util.concurrent.Executors

@Service
class TranslationService(
    private val yandexTranslationService: YandexTranslationService, private val cachingService: CachingService
// TODO: 12.09.2022  
) {
    fun translateByYandex(dataForTranslation: DataForTranslation): TranslatedData {
        val translatedDataAccumulator = synchronizedMap(mutableMapOf<String, TranslatedPair>())
        val toApiTranslate = getDataForTranslationByApi(dataForTranslation, translatedDataAccumulator)
        val pac = mutableListOf<String>()
        toApiTranslate.words.forEach {
            if (pac.size == 20) {
                println(translatedDataAccumulator.keys.toSet().size)
                translatedDataAccumulator.putAll(
                    yandexTranslationService.translate(
                        DataForTranslation(
                            dataForTranslation.originalLanguage,
                            dataForTranslation.targetLanguage,
                            pac.toList()
                        )
                    ).map { pair -> pair.original to pair }
                )
                pac.clear()
                Thread.sleep(1000)
            }
            if (translatedDataAccumulator[it] == null && pac.size < 20) {
                pac.add(it)
            }

        }
        translatedDataAccumulator.putAll(
            yandexTranslationService.translate(
                DataForTranslation(
                    dataForTranslation.originalLanguage,
                    dataForTranslation.targetLanguage,
                    pac.toList()
                )
            ).map { pair -> pair.original to pair }
        )
        val uniqueTranslatedWords =
            translatedDataAccumulator.map { translatedPair -> translatedPair.value.original to translatedPair.value.translated }
                .toMap()
        val result = dataForTranslation.words.map { original ->
            if(uniqueTranslatedWords[original] != null) {
                uniqueTranslatedWords[original]!!
            } else {
                println(original)
                ""
            }
        }
        println(dataForTranslation.words.size)
        println(result.size)
        return TranslatedData(
            result,
            translatedDataAccumulator.filter { toApiTranslate.words.contains(it.key) }.values.toList()
        )
    }

    fun getDataForTranslationByApi(
        dataForTranslation: DataForTranslation, translatedDataAccumulator: MutableMap<String, TranslatedPair>
    ): DataForTranslation {
        val originalWordTranslatedPairMap =
            cachingService.getCachedKeyValueOriginalWordAndTranslatedPairs(dataForTranslation)
        translatedDataAccumulator.putAll(originalWordTranslatedPairMap)
        val toApiTranslate = dataForTranslation.words.filter { !originalWordTranslatedPairMap.keys.contains(it) }
        return DataForTranslation(
            dataForTranslation.originalLanguage, dataForTranslation.targetLanguage, toApiTranslate
        )
    }
}