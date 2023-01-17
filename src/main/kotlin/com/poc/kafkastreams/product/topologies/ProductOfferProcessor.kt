package com.poc.kafkastreams.product.topologies

import com.poc.kafkastreams.product.model.ProductOffers
import com.poc.kafkastreams.product.model.ProductOffersFactory
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.kstream.Produced
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerde

class ProductOfferProcessor {

    companion object {
        private const val CATEGORIES_STORE = "categories-store"
    }

    operator fun invoke(
        builder: StreamsBuilder,
        inputTopic: String,
        outputTopic: String
    ): KStream<Int, ProductOffers> {
        val jsonSerde = JsonSerde<ProductOffers>()
            .apply { this.configure(mapOf(
                JsonDeserializer.TRUSTED_PACKAGES to "com.poc.kafkastreams.*",
                JsonDeserializer.VALUE_DEFAULT_TYPE to "com.poc.kafkastreams.product.model.ProductOffers"
            ), false) }

//        return builder.table(inputTopic, Materialized.with(Serdes.Integer(), jsonSerde))

//        builder.table(inputTopic, Materialized<Int, ProductOffers, KeyValueStore< Bytes, Byte[]>>.`as`(CATEGORIES_STORE).with(Serdes.Integer(), jsonSerde))

        return builder.stream(inputTopic, Consumed.with(Serdes.Integer(), jsonSerde))
            .groupByKey()
            .aggregate(
                { ProductOffersFactory.withEmptyValues() },
                { _, value, aggregate -> aggregate.process(value)},
                Materialized.with(Serdes.Integer(), jsonSerde))
            .toStream()
            .apply { this.to(outputTopic, Produced.with(Serdes.Integer(), jsonSerde)) }
    }
}