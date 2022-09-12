package com.translator.tinkoff_test_translator

import com.translator.tinkoff_test_translator.dto.DataForTranslation
import org.springframework.stereotype.Service
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.ThreadPoolExecutor

@Service
class ThreadPoolRequestService(private val threadPool: ExecutorService) {
    fun translate(dataForRequests: DataForTranslation, translator: ApiTranslationService): List<TranslatedPair> {
        val tasks = getTasks(dataForRequests, translator)
        val futures = threadPool.invokeAll(tasks)
        return getTranslatedPairs(futures)
    }

    private fun getTasks(
        dataForRequest: DataForTranslation,
        translator: ApiTranslationService
    ): List<Callable<TranslatedPair>> {
        return dataForRequest.words.map {
            Callable {
                translator.translate(
                    DataForTranslation(
                        dataForRequest.originalLanguage,
                        dataForRequest.targetLanguage,
                        listOf(it)
                    )
                )
            }
        }
    }

    private fun getTranslatedPairs(futuresList: List<Future<TranslatedPair>>): List<TranslatedPair> {
        return futuresList.map { it.get() }
    }
}