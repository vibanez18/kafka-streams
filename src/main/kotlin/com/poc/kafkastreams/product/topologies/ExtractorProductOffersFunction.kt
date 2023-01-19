package com.poc.kafkastreams.product.topologies

import com.poc.kafkastreams.product.model.ProductBestOffer
import com.poc.kafkastreams.product.model.ProductBestOffersSerde
import com.poc.kafkastreams.product.model.ProductOffersSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Produced
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class ExtractorProductOffersFunction(
    private val productOffersProcessorSupplier: ProductOffersProcessorSupplier,
    @Qualifier("productOffersKTableBuilder") private val builder: StreamsBuilder
) {

    operator fun invoke(inputTopic: String, outputTopic: String): KStream<Long, ProductBestOffer> = builder
        .stream(inputTopic, Consumed.with(Serdes.Long(), ProductOffersSerde.withTrustedPackagesAndValueType()))
        .process(productOffersProcessorSupplier)
        .apply {
            this.to(
                outputTopic,
                Produced.with(Serdes.Long(), ProductBestOffersSerde.withTrustedPackagesAndValueType())
            )
        }
}