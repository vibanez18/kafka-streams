package com.poc.kafkastreams.product.model

data class ProductOffers(
    val headers: List<String> = emptyList(),
    val product: Product
)

data class Product(
    val mmId: Long,
    val masterCategoryId: Long,
    val merchandisingCategories: MerchandisingCategories
)

data class MerchandisingCategories(
    val main: Long,
    val secondary: List<Long> = emptyList()
)
