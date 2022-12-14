package com.translator.tinkoff_test_translator.translation_api.yandex_translation

import com.translator.tinkoff_test_translator.dto.DataForTranslation
import com.translator.tinkoff_test_translator.exception.TranslateServiceException
import com.translator.tinkoff_test_translator.translation_api.ApiRequestConstraint
import com.translator.tinkoff_test_translator.translation_api.ApiTranslationService
import com.translator.tinkoff_test_translator.translation_api.YandexApiResponse
import com.translator.tinkoff_test_translator.translation_api.model.TranslatedPair
import com.translator.tinkoff_test_translator.translation_api.model.TranslationResponsePair
import com.translator.tinkoff_test_translator.translation_api.model.TranslationTaskPreparerService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity


@Service
class YandexTranslationService(
    @Value("\${url.api.yandex}") private val yandexApiUrl: String,
    @Value("\${api-key.yandex}") private val yandexApiKey: String,
    @Value("\${api-key.header-key.yandex}") private val yandexHeaderKey: String,
    @Value("\${constraint.yandex.numberOfRequestsPerTime}") private val numberOfRequestsPerTime: Int,
    @Value("\${constraint.yandex.time}") private val time: Long,
    private val httpClient: RestTemplate,
    private val translationTaskPreparer: TranslationTaskPreparerService,
) : ApiTranslationService {
    private val headers = translationTaskPreparer.getHeaders(mapOf(Pair(yandexHeaderKey, yandexApiKey)))
    override fun translate(dataForTranslation: DataForTranslation): List<TranslatedPair> {
        val request = getRequest(dataForTranslation)
        val entity = HttpEntity(
            request, headers
        )
        val response =
            httpClient.runCatching { postForEntity<YandexApiResponse>(yandexApiUrl, entity) }
                .getOrElse {
                    throw TranslateServiceException(YandexTranslationService::class.toString(), it.message ?: "")
                }
        return getTranslatedPairs(TranslationResponsePair(entity, response))
    }

    override fun getConstraint(): ApiRequestConstraint {
        return ApiRequestConstraint(numberOfRequestsPerTime, time)
    }

    private fun getRequest(dataForTranslation: DataForTranslation): YandexApiReqest {
        return YandexApiReqest(
            dataForTranslation.originalLanguage,
            dataForTranslation.targetLanguage,
            dataForTranslation.words
        )
    }

    private fun getTranslatedPairs(translatedResponsePair: TranslationResponsePair<YandexApiReqest, YandexApiResponse>): List<TranslatedPair> {
        return translatedResponsePair.request.body!!.texts.zip(translatedResponsePair.response.body!!.translations)
            .map { TranslatedPair(it.first, it.second.text) }
    }

}