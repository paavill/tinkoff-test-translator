package com.translator.tinkoff_test_translator

import com.translator.tinkoff_test_translator.dto.DataForTranslation

interface ApiTranslationService {
    fun translate(dataForTranslation: DataForTranslation): TranslatedPair
    fun getConstraint(): ApiRequestConstraint
}