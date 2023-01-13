package com.poc.kafkastreams.processor

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Grouped
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.kstream.Produced

class FatEventCountWordsProcessor {

    companion object {
        private const val COUNTS_WORDS_STORE = "count-words-store"
    }

    operator fun invoke(builder: StreamsBuilder,
                        inputTopic: String,
                        outputTopic: String): KStream<String?, Long> =
        builder.stream(inputTopic, Consumed.with(Serdes.Integer(), Serdes.String()))
            .flatMapValues { value: String -> value.lowercase().split("\\W+") }
            .map { _: Int?, value: String? -> KeyValue(value, value) }
            .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
            .count(Materialized.`as`(COUNTS_WORDS_STORE))
            .toStream()
            .apply { this.to(outputTopic, Produced.with(Serdes.String(), Serdes.Long())) }
}
