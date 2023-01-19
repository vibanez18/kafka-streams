package com.poc.kafkastreams.product.topologies


import com.poc.kafkastreams.product.model.ProductBestOffer
import com.poc.kafkastreams.product.model.ProductOffers
import com.poc.kafkastreams.product.model.ProductOffersSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.processor.api.Processor
import org.apache.kafka.streams.processor.api.ProcessorSupplier
import org.apache.kafka.streams.state.StoreBuilder
import org.apache.kafka.streams.state.Stores
import org.springframework.stereotype.Component

@Component
class ProductOffersProcessorSupplier: ProcessorSupplier<Long, ProductOffers, Long, ProductBestOffer> {
    companion object {
        const val STORE_NAME = "ProductOffers-BestOffers"
        val STORE_BUILDER = Stores.keyValueStoreBuilder(
            Stores.persistentKeyValueStore(STORE_NAME),
            Serdes.Long(),
            ProductOffersSerde.withTrustedPackagesAndValueType()
        )!!
    }

    override fun stores(): MutableSet<StoreBuilder<*>> {
        return mutableSetOf(STORE_BUILDER)
    }

    override fun get(): Processor<Long, ProductOffers, Long, ProductBestOffer> = ProductOffersProcessor()
}