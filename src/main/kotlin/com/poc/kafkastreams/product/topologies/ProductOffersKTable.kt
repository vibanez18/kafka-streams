package com.poc.kafkastreams.product.topologies

import com.poc.kafkastreams.product.model.ProductOffers
import com.poc.kafkastreams.product.model.ProductOffersSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.state.KeyValueStore
import org.springframework.kafka.support.serializer.JsonSerde

class ProductOffersKTable {

    companion object {
        const val PRODUCT_OFFERS_KTABLE = "product-offers-ktable"
    }

    operator fun invoke(builder: StreamsBuilder,
                        inputTopic: String): KTable<Int, ProductOffers> =
        builder.table(inputTopic, createdMaterialized(ProductOffersSerde.withTrustedPackagesAndValueType()))

    private fun createdMaterialized(jsonSerde: JsonSerde<ProductOffers>) =
        Materialized.`as`<Int?, ProductOffers?, KeyValueStore<Bytes, ByteArray>?>(PRODUCT_OFFERS_KTABLE)
            .withKeySerde(Serdes.Integer())
            .withValueSerde(jsonSerde)
}