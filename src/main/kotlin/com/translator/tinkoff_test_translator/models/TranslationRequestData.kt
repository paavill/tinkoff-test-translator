package com.translator.tinkoff_test_translator.models

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import javax.persistence.*

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "input_data")
data class TranslationRequestData(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long


)