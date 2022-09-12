package com.translator.tinkoff_test_translator

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import java.util.concurrent.Callable

@Service
class TranslationTaskPreparerService(val httpClient: RestTemplate) {
    fun getHeaders(headersKeyValues: Map<String, String>): HttpHeaders {
        val headers = HttpHeaders()
        headersKeyValues.forEach { (key, value) ->
            headers[key] = value
        }
        return headers
    }

    final inline fun <reified T, reified Q> getTasks(
        headers: HttpHeaders,
        requests: List<T>,
        url: String
    ): List<Callable<TranslationResponsePair<T, Q>>> {
        return requests.map { request ->
            Callable {
                val entity = HttpEntity<T>(
                    request, headers
                )

                val response =
                    httpClient.runCatching { postForEntity<Q>(url, entity) }.getOrElse {
                        println(it.message)
                        null!!
                    }
                // TODO: 11.09.2022
                TranslationResponsePair(entity, response)
            }

        }
    }
}