package com.translator.tinkoff_test_translator.models

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "translated_word")
class TranslatedWord(
    @Id
    val originalWord: String,
    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    val translationRequest: TranslationRequestData,
    val targetWord: String,
)

