package com.translator.tinkoff_test_translator.yandex_translation

import com.translator.tinkoff_test_translator.TranslatedPair
import com.translator.tinkoff_test_translator.TranslationResponsePair
import com.translator.tinkoff_test_translator.TranslationTaskPreparerService
import com.translator.tinkoff_test_translator.dto.DataForTranslation
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.http.HttpHeaders
import java.util.Collections.synchronizedList
import java.util.LinkedList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future


@Service
class YandexTranslationService(
    @Value("\${url.api.yandex}") private val yandexApiUrl: String,
    @Value("\${api-key.yandex}") private val yandexApiKey: String,
    @Value("\${api-key.header-key.yandex}") private val yandexHeaderKey: String,
    private val threadPool: ExecutorService,
    private val translationTaskPreparer: TranslationTaskPreparerService,
)
{
    private val headers = translationTaskPreparer.getHeaders(mapOf(Pair(yandexHeaderKey, yandexApiKey)))

    fun translate(dataForTranslation: DataForTranslation): List<TranslatedPair> {
        println(dataForTranslation.words.size)
        val requests = getRequestOnEachWord(dataForTranslation)
        val tasks = translationTaskPreparer.getTasks<YandexApiReqest, YandexApiResponse>(
            headers,
            requests,
            yandexApiUrl
        )
        val futures = threadPool.invokeAll(tasks)

        return getTranslatedPairs(futures)
    }

    private fun getRequestOnEachWord(dataForTranslation: DataForTranslation): List<YandexApiReqest> {
        return dataForTranslation.words.toSet().map { word ->
            YandexApiReqest(
                dataForTranslation.originalLanguage,
                dataForTranslation.targetLanguage,
                listOf(word)
            )
        }
    }

    private fun getTranslatedPairs(futures: List<Future<TranslationResponsePair<YandexApiReqest, YandexApiResponse>>>): List<TranslatedPair> {
        val translatedPairs = futures.map { future ->
            TranslatedPair(
                future.get().request.body!!.texts.joinToString(" "),
                future.get().response.body!!.translations.joinToString(" ") { it.text })
        }
        return translatedPairs
    }

}