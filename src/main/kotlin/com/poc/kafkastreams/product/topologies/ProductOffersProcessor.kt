package com.poc.kafkastreams.product.topologies

import com.poc.kafkastreams.product.model.ProductBestOffer
import com.poc.kafkastreams.product.model.ProductBestOfferFactory
import com.poc.kafkastreams.product.model.ProductOffers
import com.poc.kafkastreams.product.producer.HeaderType
import org.apache.kafka.common.header.Headers
import org.apache.kafka.common.header.internals.RecordHeaders
import org.apache.kafka.streams.processor.api.Processor
import org.apache.kafka.streams.processor.api.ProcessorContext
import org.apache.kafka.streams.processor.api.Record
import org.apache.kafka.streams.state.KeyValueStore
import java.lang.RuntimeException
import java.time.Instant

class ProductOffersProcessor: Processor<Long, ProductOffers, Long, ProductBestOffer> {
    private lateinit var context: ProcessorContext<Long, ProductBestOffer>
    private lateinit var bestOfferStore: KeyValueStore<Long, ProductBestOffer>

    override fun init(context: ProcessorContext<Long, ProductBestOffer>) {
        this.context = context
        bestOfferStore = context.getStateStore(ProductOffersProcessorSupplier.STORE_NAME)
    }

    override fun process(productOffersRecord: Record<Long, ProductOffers>) {
        val stateHeaderValue = String(productOffersRecord.headers().lastHeader(Companion.MM_EVENT_STATE_HEADER).value())
        val productOffers = productOffersRecord.value()

        when (stateHeaderValue) {
            HeaderType.CREATED.name -> this.handleOnCreatedEvent(productOffers)
            HeaderType.UPDATED.name -> this.handleOnUpdatedEvent(productOffers)
            HeaderType.DELETED.name -> this.handleOnDeleteEvent(productOffers)
            else -> RuntimeException("Header no present on msg")
        }
    }

    private fun putOnStoreAndForwardBestOfferEvent(productOffers: ProductOffers, headers: Headers) {
        val productOffersMMID = productOffers.mmId
        val productOffersBestOffer = productOffers.sellerSpecificInformation[0].offer
        val productBestOffer =
            ProductBestOfferFactory.fromProductOffers(productOffers, productOffersBestOffer)

        this.bestOfferStore.put(productOffersMMID, productBestOffer)
        this.context.forward(
            Record(
                productOffersMMID,
                productBestOffer,
                Instant.now().toEpochMilli()
            ).withHeaders(headers)
        )
    }

    private fun handleOnCreatedEvent(productOffers: ProductOffers) =
        this.putOnStoreAndForwardBestOfferEvent(
            productOffers,
            RecordHeaders().add(Companion.BEST_OFFER_EVENT_HEADER, BestOfferHeaders.LIFT_CREATED.name.encodeToByteArray())
        )

    private fun handleOnUpdatedEvent(productOffers: ProductOffers) {

        if (bestOfferStore.get(productOffers.mmId) != null) {
            bestOfferStore.delete(productOffers.mmId)
        }

        this.putOnStoreAndForwardBestOfferEvent(
            productOffers,
            RecordHeaders().add(Companion.BEST_OFFER_EVENT_HEADER, BestOfferHeaders.LIFT_UPDATED.name.encodeToByteArray())
        )
    }

    private fun handleOnDeleteEvent(productOffers: ProductOffers) {
        val productOffersMMID = productOffers.mmId
        bestOfferStore.delete(productOffers.mmId)
        this.context.forward(
            Record(
                productOffersMMID,
                ProductBestOffer(mmId = productOffersMMID),
                Instant.now().toEpochMilli()
            ).withHeaders(
                RecordHeaders().add(Companion.BEST_OFFER_EVENT_HEADER, BestOfferHeaders.LIFT_DELETED.name.encodeToByteArray())
            )
        )
    }

    companion object {
        private const val MM_EVENT_STATE_HEADER = "mm-event-state"
        private const val BEST_OFFER_EVENT_HEADER = "lift-best-offer-event-state"
    }
}

enum class BestOfferHeaders { LIFT_CREATED, LIFT_UPDATED, LIFT_DELETED }