package com.translator.tinkoff_test_translator.translation_api.service

import com.translator.tinkoff_test_translator.dto.DataForTranslation
import com.translator.tinkoff_test_translator.translation_api.ApiTranslationService
import com.translator.tinkoff_test_translator.translation_api.model.TranslatedPair

abstract class AbstractRequestService {
    abstract val wordsByRequest: Int

    abstract fun translate(dataForRequests: DataForTranslation, translator: ApiTranslationService): List<TranslatedPair>
}