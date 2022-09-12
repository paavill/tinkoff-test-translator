package com.translator.tinkoff_test_translator

import com.translator.tinkoff_test_translator.dto.TranslationRequest
import com.translator.tinkoff_test_translator.dto.TranslationResponse
import com.translator.tinkoff_test_translator.dto.mappers.TranslatedDataToTranslationResponseMapper
import com.translator.tinkoff_test_translator.dto.mappers.TranslationRequestToDataForTranslationMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


@RestController
class TranslatorController(
    private val translationService: TranslationService,
    private val translationRequestToDataForTranslationMapper: TranslationRequestToDataForTranslationMapper,
    private val translatedDataToTranslationResponseMapper: TranslatedDataToTranslationResponseMapper,
    private val cachingService: CachingService
) {

    @PostMapping("yandex-translator")

    fun translateByYandex(@RequestBody requestData: TranslationRequest, request: HttpServletRequest): ResponseEntity<TranslationResponse> {
        val dataForTranslation = translationRequestToDataForTranslationMapper.map(requestData)
        val translatedData = translationService.translateByYandex(dataForTranslation)
        val response = translatedDataToTranslationResponseMapper.map(translatedData)
        cachingService.cache(requestData, response, request, translatedData)
        return ResponseEntity.ok(response)
    }

}