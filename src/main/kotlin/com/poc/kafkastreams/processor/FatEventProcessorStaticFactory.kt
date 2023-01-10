package com.poc.kafkastreams.processor

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Grouped
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Materialized


class FatEventProcessorStaticFactory private constructor() {
//    companion object {
//        private const val COUNTS_WORDS_STORE = "count-words-store"
//
//        fun withInputAndOutputTopic(inputTopic: String, outputTopic: String) =
//            FatEventProcessor(inputTopic, outputTopic)
//
//        fun withStreamBuilderAndInputTopic(builder: StreamsBuilder,
//                                           inputTopic: String): KStream<String?, Long> =
//            builder.stream(inputTopic, Consumed.with(Serdes.String(), Serdes.String()))
//                .flatMapValues { value: String -> value.lowercase().split("\\W+") }
//                .map { _: String?, value: String? -> KeyValue(value, value)}
//                .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
//                .count(Materialized.`as`(COUNTS_WORDS_STORE))
//                .toStream()
//    }
}