package com.translator.tinkoff_test_translator.models

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "translation_request_data")
class TranslationRequestData(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    @OneToMany(mappedBy = "translationRequest")
    val translatedWords: List<TranslatedWord>? = null,
    val originalLanguage: String,
    val targetLanguage: String,
    @Column(length = 100000)
    val originalString: String,
    @Column(length = 100000)
    val translatedString: String,
    val ipAddress: String,
    val requestTime: Date
)