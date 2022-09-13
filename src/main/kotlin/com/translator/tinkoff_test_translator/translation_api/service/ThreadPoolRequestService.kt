package com.translator.tinkoff_test_translator.translation_api.service

import com.translator.tinkoff_test_translator.translation_api.ApiTranslationService
import com.translator.tinkoff_test_translator.translation_api.model.TranslatedPair
import com.translator.tinkoff_test_translator.dto.DataForTranslation
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

@Service
class ThreadPoolRequestService(
    private val threadPool: ExecutorService,
    @Value("\${thread_pool.thread_number}")
    private val threadPoolThreadNumber: Int,
    @Value("\${words_by_request}")
    override val wordsByRequest: Int
) : AbstractRequestService() {
    override fun translate(dataForRequests: DataForTranslation, translator: ApiTranslationService): List<TranslatedPair> {
        val tasks = getTasks(dataForRequests, translator)
        val futures = threadPool.invokeAll(tasks)
        return getTranslatedPairs(futures)
    }

    private fun getTasks(
        dataForRequest: DataForTranslation,
        translator: ApiTranslationService
    ): List<Callable<List<TranslatedPair>>> {
        return dataForRequest.words.chunked(wordsByRequest) .map {
            Callable {
                val constraint = translator.getConstraint()
                Thread.sleep(constraint.time/(constraint.numberOfRequestsPerTime/threadPoolThreadNumber))
                val result = translator.translate(
                    DataForTranslation(
                        dataForRequest.originalLanguage,
                        dataForRequest.targetLanguage,
                        it
                    )
                )
                result
            }
        }
    }

    private fun getTranslatedPairs(futuresList: List<Future<List<TranslatedPair>>>): List<TranslatedPair> {
        return futuresList.map { it.get() }.flatten()
    }
}