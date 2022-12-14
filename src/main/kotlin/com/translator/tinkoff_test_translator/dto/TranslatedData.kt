package com.translator.tinkoff_test_translator.dto

import com.translator.tinkoff_test_translator.translation_api.model.TranslatedPair

data class TranslatedData(val translatedWords: List<String>, val apiTranslatedPairs: List<TranslatedPair>)