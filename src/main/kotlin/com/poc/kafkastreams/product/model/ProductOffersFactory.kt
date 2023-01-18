package com.poc.kafkastreams.product.model

import com.poc.kafkastreams.configuration.randomLong
import com.poc.kafkastreams.configuration.randomLongs
import com.poc.kafkastreams.configuration.randomUUID
import io.github.serpro69.kfaker.Faker


class ProductOffersFactory private constructor() {
    companion object {
        private const val MIN_PRICE = 10
        private const val MAX_PRICE = 100

        fun withRandomCategories(): ProductOffers {
            val randomCategory = Long.randomLong()
            return ProductOffers(
                mmId = Long.randomLong(),
                masterCategoryId = randomCategory,
                merchandisingCategories = MerchandisingCategories(
                    main = randomCategory,
                    secondary = Long.randomLongs()
                ),
                sellerSpecificInformation = listOf(createSellerSpecificInformation())
            )
        }

        private fun createSellerSpecificInformation() = SellerSpecificInformation(
            offer = Offer(
                offerId = String.randomUUID(),
                pricing = Pricing(
                    priceVatIncluded = Faker().random.nextInt(MIN_PRICE, MAX_PRICE).toDouble(),
                    currencySymbol = "€"
                )
            )
        )
//        fun withEmptyValues(): ProductOffers = ProductOffers(
//            mmId = 0,
//            masterCategoryId = 0,
//            merchandisingCategories = MerchandisingCategories(
//                main = 0
//            )
//        )
    }
}