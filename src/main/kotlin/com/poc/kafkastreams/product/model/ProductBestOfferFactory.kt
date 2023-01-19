package com.poc.kafkastreams.product.model

class ProductBestOfferFactory private constructor() {
    companion object {
        fun fromProductOffers(productOffers: ProductOffers, bestOffer: Offer) =
            ProductBestOffer(
                mmId = productOffers.mmId,
                merchandisingCategories = productOffers.merchandisingCategories,
                bestOffer = bestOffer
            )
    }
}