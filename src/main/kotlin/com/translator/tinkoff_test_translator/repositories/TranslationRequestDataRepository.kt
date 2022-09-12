package com.translator.tinkoff_test_translator.repositories

import com.translator.tinkoff_test_translator.models.TranslationRequestData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TranslationRequestDataRepository : JpaRepository<TranslationRequestData, Long>