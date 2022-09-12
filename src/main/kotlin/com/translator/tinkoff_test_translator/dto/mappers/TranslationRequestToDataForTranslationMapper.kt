package com.translator.tinkoff_test_translator.dto.mappers

import com.translator.tinkoff_test_translator.dto.DataForTranslation
import com.translator.tinkoff_test_translator.controller.TranslationRequest
import org.springframework.stereotype.Component

@Component
class TranslationRequestToDataForTranslationMapper {
    fun map(request: TranslationRequest): DataForTranslation {
        val words = request.translatedString.trim().split(" ")
        return DataForTranslation(request.originalLanguage, request.targetLanguage, words)
    }
}