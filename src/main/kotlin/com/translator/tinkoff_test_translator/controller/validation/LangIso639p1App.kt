package com.translator.tinkoff_test_translator.controller.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [LangIso639p1AppValidator::class])
@MustBeDocumented
annotation class LangIso639p1App(
    val message: String = "language must be ISO-639-1 standard",

    val groups: Array<KClass<*>> = [],

    val payload: Array<KClass<out Payload>> = []
)