package com.poc.kafkastreams.product.model

import com.poc.kafkastreams.configuration.randomLong
import com.poc.kafkastreams.configuration.randomLongs


class ProductOffersFactory private constructor() {
    companion object {
        fun withRandomCategories(): ProductOffers {
            val randomCategory = Long.randomLong()
            return ProductOffers(
                mmId = Long.randomLong(),
                masterCategoryId = randomCategory,
                merchandisingCategories = MerchandisingCategories(
                    main = randomCategory,
                    secondary = Long.randomLongs()
                )
            )
        }
        fun withEmptyValues(): ProductOffers = ProductOffers(
            mmId = 0,
            masterCategoryId = 0,
            merchandisingCategories = MerchandisingCategories(
                main = 0
            )
        )
    }
}