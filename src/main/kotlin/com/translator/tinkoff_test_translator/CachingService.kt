package com.translator.tinkoff_test_translator

import com.translator.tinkoff_test_translator.dto.DataForTranslation
import com.translator.tinkoff_test_translator.dto.TranslatedData
import com.translator.tinkoff_test_translator.dto.TranslationRequest
import com.translator.tinkoff_test_translator.dto.TranslationResponse
import com.translator.tinkoff_test_translator.models.TranslatedWord
import com.translator.tinkoff_test_translator.models.TranslationRequestData
import com.translator.tinkoff_test_translator.repositories.TranslatedWordRepository
import com.translator.tinkoff_test_translator.repositories.TranslationRequestDataRepository
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
            dataForTranslation.words.toSet()
        ).associate { it.originalWord to TranslatedPair(it.originalWord, it.targetWord) }
    }
}