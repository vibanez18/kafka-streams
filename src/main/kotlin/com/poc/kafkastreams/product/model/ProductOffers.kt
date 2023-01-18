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
    @JsonProperty("sellerSpecificInformation")
    val sellerSpecificInformation: List<SellerSpecificInformation>
)

data class MerchandisingCategories(
    @JsonProperty("main")
    val main: Long,
    @JsonProperty("secondary")
    val secondary: List<Long> = emptyList()
)

data class SellerSpecificInformation(@JsonProperty("offer") val offer: Offer)

data class Offer(
    @JsonProperty("offerId")
    val offerId: String,
    @JsonProperty("pricing")
    val pricing: Pricing)

data class Pricing(
    @JsonProperty("priceVatIncluded")
    val priceVatIncluded: Double,
    @JsonProperty("currencySymbol")
    val currencySymbol: String)


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
