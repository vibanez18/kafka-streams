package com.poc.kafkastreams.product.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductOffers(
    @JsonProperty("headers")
    val headers: List<String> = emptyList(),
    @JsonProperty("product")
    val product: Product
)

data class Product(
    @JsonProperty("mmId")
    val mmId: Long,
    @JsonProperty("masterCategoryId")
    val masterCategoryId: Long,
    @JsonProperty("merchandisingCategories")
    val merchandisingCategories: MerchandisingCategories
)

data class MerchandisingCategories(
    @JsonProperty("main")
    val main: Long,
    @JsonProperty("secondary")
    val secondary: List<Long> = emptyList()
)
