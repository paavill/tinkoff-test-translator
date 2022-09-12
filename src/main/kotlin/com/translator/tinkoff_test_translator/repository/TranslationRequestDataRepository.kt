package com.translator.tinkoff_test_translator.repository

import com.translator.tinkoff_test_translator.model.TranslationRequestData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TranslationRequestDataRepository : JpaRepository<TranslationRequestData, Long>