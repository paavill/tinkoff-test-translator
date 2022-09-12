package com.translator.tinkoff_test_translator.service

import com.translator.tinkoff_test_translator.translation_api.model.TranslatedPair
import com.translator.tinkoff_test_translator.dto.DataForTranslation
import com.translator.tinkoff_test_translator.dto.TranslatedData
import com.translator.tinkoff_test_translator.controller.TranslationRequest
import com.translator.tinkoff_test_translator.controller.TranslationResponse
import com.translator.tinkoff_test_translator.model.TranslatedWord
import com.translator.tinkoff_test_translator.model.TranslationRequestData
import com.translator.tinkoff_test_translator.repository.TranslatedWordRepository
import com.translator.tinkoff_test_translator.repository.TranslationRequestDataRepository
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
class CachingService(
    private val translationRequestDataRepository: TranslationRequestDataRepository,
    private val translatedWordRepository: TranslatedWordRepository
) {
    fun cache(
        requestData: TranslationRequest,
        translationResponse: TranslationResponse,
        request: HttpServletRequest,
        translatedData: TranslatedData
    ) {
        val requestDataToRepository = TranslationRequestData(
            originalLanguage = requestData.originalLanguage,
            targetLanguage = requestData.targetLanguage,
            originalString = requestData.translatedString,
            translatedString = translationResponse.translatedString,
            ipAddress = request.remoteAddr,
            requestTime = Date(request.getDateHeader("Date"))
        )

        translationRequestDataRepository.save(requestDataToRepository)
        translatedData.apiTranslatedPairs.forEach {
            translatedWordRepository.save(TranslatedWord(it.original, requestDataToRepository, it.translated))
        }
    }

    fun getCachedKeyValueOriginalWordAndTranslatedPairs(dataForTranslation: DataForTranslation): Map<String, TranslatedPair> {
        return translatedWordRepository.getTranslatedWordsByOriginalWordsAndTargetLanguage(
            dataForTranslation.targetLanguage,
            dataForTranslation.words,
        ).associate { it.originalWord to TranslatedPair(it.originalWord, it.targetWord) }
    }
}