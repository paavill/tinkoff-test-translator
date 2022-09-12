package com.translator.tinkoff_test_translator.controller.validation

import java.util.*
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class LangIso639p1AppValidator : ConstraintValidator<LangIso639p1App?, String?> {
    private val isoCodes: List<String> = Locale.getISOLanguages().toList()
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return if (value == null) {
            true
        } else isoCodes.contains(value)
    }
}