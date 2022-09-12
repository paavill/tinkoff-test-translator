package com.translator.tinkoff_test_translator.exception

class TranslateServiceException(val service: String, val info: String) : Exception()