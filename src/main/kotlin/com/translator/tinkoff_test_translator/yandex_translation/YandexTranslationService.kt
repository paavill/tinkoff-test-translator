package com.translator.tinkoff_test_translator.yandex_translation

import com.translator.tinkoff_test_translator.dto.DataForTranslation
import com.translator.tinkoff_test_translator.dto.TranslatedData
import com.translator.tinkoff_test_translator.dto.mappers.YandexApiResponseToTranslatedDataMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService


@Service
class YandexTranslationService(
    @Value("\${url.api.yandex}") private val yandexApiUrl: String,
    @Value("\${api-key.yandex}") private val yandexApiKey: String,
    private val httpClient: RestTemplate,
    private val threadPool: ExecutorService,
    private val mapper: YandexApiResponseToTranslatedDataMapper
) {

    fun translate(dataForTranslation: DataForTranslation): TranslatedData {
        val resultResponseList = mutableListOf<Word>()
        val tasks = getTasks(dataForTranslation, resultResponseList)
        threadPool.invokeAll(tasks)
        println(dataForTranslation.words.size)
        println(resultResponseList.size)
        val response = YandexApiResponse(resultResponseList)

        return mapper.map(response)
    }

    private fun getHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers["Authorization"] = yandexApiKey
        return headers
    }

    private fun getTasks(
        dataForTranslation: DataForTranslation,
        resultResponseList: MutableList<Word>
    ): List<Callable<Boolean>> {
        return dataForTranslation.words.map { word ->
            Callable {
                val entity = HttpEntity<YandexApiReqest>(
                    YandexApiReqest(
                        dataForTranslation.originalLanguage, dataForTranslation.targetLanguage, listOf(word)
                    ), getHeaders()
                )
                val response = httpClient.postForEntity<YandexApiResponse>(yandexApiUrl, entity)
                // TODO: 11.09.2022
                resultResponseList.addAll(response.body!!.translations)
            }

        }
    }

}