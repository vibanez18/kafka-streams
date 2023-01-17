package com.poc.kafkastreams.product.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerde

data class ProductOffers(
    @JsonProperty("mmId")
    val mmId: Long,
    @JsonProperty("masterCategoryId")
    val masterCategoryId: Long,
    @JsonProperty("merchandisingCategories")
    val merchandisingCategories: MerchandisingCategories,
    @JsonProperty("state")
    val state: ProductOffersState = ProductOffersState.DEFAULT
) {
    fun process(offer: ProductOffers): ProductOffers = if (this.masterCategoryId == offer.masterCategoryId
        && this.merchandisingCategories.main == offer.merchandisingCategories.main
        && this.merchandisingCategories.secondary.containsAll(offer.merchandisingCategories.secondary)
    )
        this.copy(state = ProductOffersState.MATCH)
    else
        this.copy(state = ProductOffersState.NO_MATCH)
}

data class MerchandisingCategories(
    @JsonProperty("main")
    val main: Long,
    @JsonProperty("secondary")
    val secondary: List<Long> = emptyList()
)

enum class ProductOffersState { DEFAULT, MATCH, NO_MATCH }

class ProductOffersSerde private constructor(){
    companion object {
        fun withTrustedPackagesAndValueType(): JsonSerde<ProductOffers> =
            JsonSerde<ProductOffers>()
                .apply { this.configure(mapOf(
                    JsonDeserializer.TRUSTED_PACKAGES to "com.poc.kafkastreams.*",
                    JsonDeserializer.VALUE_DEFAULT_TYPE to "com.poc.kafkastreams.product.model.ProductOffers"
                ), false) }
    }
}
