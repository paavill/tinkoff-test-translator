package com.translator.tinkoff_test_translator.repository

import com.translator.tinkoff_test_translator.model.TranslatedWord
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TranslatedWordRepository : CrudRepository<TranslatedWord, String> {
    @Query(
        "select words from TranslatedWord words " +
                "where words.translationRequest.targetLanguage=:targetLanguage and words.originalWord in :originalWords"
    )
    fun getTranslatedWordsByOriginalWordsAndTargetLanguage(
        @Param("targetLanguage") targetLanguage: String,
        @Param("originalWords") originalWords: List<String>
    ): List<TranslatedWord>
}