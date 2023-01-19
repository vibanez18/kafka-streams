package com.poc.kafkastreams.product.topologies

import com.poc.kafkastreams.product.model.ProductBestOffer
import com.poc.kafkastreams.product.model.ProductBestOffersSerde
import com.poc.kafkastreams.product.model.ProductOffersSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Produced
import org.springframework.stereotype.Component

@Component
class ExtractorProductOffersFunction(private val productOffersProcessorSupplier: ProductOffersProcessorSupplier) {

//    companion object {
//        const val PRODUCT_OFFERS_KTABLE = "product-offers-ktable"
//    }

    operator fun invoke(
        builder: StreamsBuilder,
        inputTopic: String,
        outputTopic: String
    ): KStream<Long, ProductBestOffer> = builder
        .stream(inputTopic, Consumed.with(Serdes.Long(), ProductOffersSerde.withTrustedPackagesAndValueType()))
        .process(productOffersProcessorSupplier)
        .apply {
            this.to(
                outputTopic,
                Produced.with(Serdes.Long(), ProductBestOffersSerde.withTrustedPackagesAndValueType()))
        }
//        .apply { extractBestOfferAndPublishOnTopic() }
//        .apply { extractCategoriesAndPublishOnKTable(outputTopic) }

//    private fun KStream<Long, ProductOffers>.extractBestOfferAndPublishOnTopic() = this
//        .map { _, value -> KeyValue.pair(value.mmId, value.sellerSpecificInformation[0].offer.offerId) }
//        .toTable(createdMaterialized())
//
//    private fun KStream<Long, ProductOffers>.extractCategoriesAndPublishOnKTable(outputTopic: String) = this
//        .map { _, value -> KeyValue.pair(value.mmId, value.masterCategoryId) }
//        .to(outputTopic, Produced.with(Serdes.Long(), Serdes.Long()))

//    private fun createdMaterialized() =
//        Materialized.`as`<Long, String, KeyValueStore<Bytes, ByteArray>>(PRODUCT_OFFERS_KTABLE)
//            .withKeySerde(Serdes.Long())
//            .withValueSerde(Serdes.String())

}