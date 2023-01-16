package com.poc.kafkastreams.product.producer

import com.poc.kafkastreams.configuration.randomLong
import com.poc.kafkastreams.configuration.randomLongs
import com.poc.kafkastreams.product.model.MerchandisingCategories
import com.poc.kafkastreams.product.model.Product
import com.poc.kafkastreams.product.model.ProductOffers


class ProductOffersFactory private constructor() {
    companion object {
        fun withRandomCategories(): ProductOffers {
            val randomCategory = Long.randomLong()
            return ProductOffers(
                product = Product(
                    mmId = Long.randomLong(),
                    masterCategoryId = randomCategory,
                    merchandisingCategories = MerchandisingCategories(
                        main = randomCategory,
                        secondary = Long.randomLongs()
                    )
                )
            )
        }
    }
}