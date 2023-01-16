package com.poc.kafkastreams.product.processor

import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KStream

class ProductOfferProcessor {

    companion object {
        private const val CATEGORIES_STORE = ""
    }

    operator fun invoke(
        builder: StreamsBuilder,
        inputTopic: String,
        outputTopic: String
    ): KStream<String?, String>? {
        return null
    }
}