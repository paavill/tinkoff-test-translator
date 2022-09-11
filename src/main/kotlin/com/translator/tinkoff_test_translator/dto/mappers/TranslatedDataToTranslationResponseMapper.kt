package com.translator.tinkoff_test_translator.dto.mappers

import com.translator.tinkoff_test_translator.dto.TranslatedData
import com.translator.tinkoff_test_translator.dto.TranslationResponse
import org.springframework.stereotype.Component

@Component
class TranslatedDataToTranslationResponseMapper {
    fun map(translatedData: TranslatedData): TranslationResponse {
        val translatedString = translatedData.words.reduce { first, second ->
            "$first $second"
        }
        return TranslationResponse(translatedString)
    }
}