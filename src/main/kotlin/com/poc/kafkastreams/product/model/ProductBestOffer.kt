package com.poc.kafkastreams.product.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerde

data class ProductBestOffer(
    @JsonProperty("mmId")
    val mmId: Long,
    @JsonProperty("merchandisingCategories")
    val merchandisingCategories: MerchandisingCategories? = null,
    @JsonProperty("bestOffer")
    val bestOffer: Offer? = null
)

class ProductBestOffersSerde private constructor(){
    companion object {
        fun withTrustedPackagesAndValueType(): JsonSerde<ProductBestOffer> =
            JsonSerde<ProductBestOffer>()
                .apply { this.configure(mapOf(
                    JsonDeserializer.TRUSTED_PACKAGES to "com.poc.kafkastreams.*",
                    JsonDeserializer.VALUE_DEFAULT_TYPE to "com.poc.kafkastreams.product.model.ProductBestOffer"
                ), false) }
    }
}