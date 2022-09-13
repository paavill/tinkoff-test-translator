package com.translator.tinkoff_test_translator.translation_api

import com.translator.tinkoff_test_translator.translation_api.model.TranslatedPair
import com.translator.tinkoff_test_translator.dto.DataForTranslation

interface ApiTranslationService {
    fun translate(dataForTranslation: DataForTranslation): List<TranslatedPair>
    fun getConstraint(): ApiRequestConstraint
}