package com.poc.kafkastreams.product.producer

import com.poc.kafkastreams.configuration.randomLong
import com.poc.kafkastreams.product.configuration.ProductOffersStreamsConfig
import com.poc.kafkastreams.product.model.ProductOffers
import com.poc.kafkastreams.product.model.ProductOffersFactory
import io.github.serpro69.kfaker.Faker
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.internals.RecordHeaders
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.concurrent.CompletableFuture
import java.util.stream.Stream
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

@Component
@Profile("product-offers")
class ProductOffersProducer(
    @Qualifier("product-offers") private val kafkaTemplate: KafkaTemplate<Long, ProductOffers>
): Logging {
    companion object {
        private const val MM_EVENT_STATE_HEADER = "mm-event-state"
        val mmIdsCache = mutableListOf<Long>()
    }

    @EventListener(ApplicationStartedEvent::class)
    operator fun invoke(): Flux<CompletableFuture<SendResult<Long, ProductOffers>>> = Flux.zip(
        Flux.interval(5000.milliseconds.toJavaDuration()),
        Flux.fromStream(Stream.generate { createProductOffers() })
    ).map { kafkaTemplate.send(it.t2) }


    private fun createProductOffers(): ProducerRecord<Long, ProductOffers> {
        val productOffers = ProductOffersFactory.withRandomCategories()
        val randomHeader = if (!mmIdsCache.contains(productOffers.mmId))
            HeaderType.CREATED else
                HeaderType.values().random()
        val randomHeaderByteArray = RecordHeaders().add(MM_EVENT_STATE_HEADER, randomHeader.name.toByteArray())
        val producerRecord: ProducerRecord<Long, ProductOffers>

        when(randomHeader) {
            HeaderType.CREATED -> {
                producerRecord = createProducerRecord(productOffers, randomHeaderByteArray.first())
                mmIdsCache.add(productOffers.mmId)
                logger.info("Sending CREATED event with mmId: ${productOffers.mmId}")
            }
            HeaderType.UPDATED -> {
                val updatedProductOffer = productOffers.copy(masterCategoryId = Long.randomLong())
                producerRecord = createProducerRecord(updatedProductOffer, randomHeaderByteArray.first())
                logger.info("Sending UPDATED event with mmId: ${productOffers.mmId}")
            }
            HeaderType.DELETED -> {
                producerRecord = createProducerRecord(productOffers.copy(), randomHeaderByteArray.first())
                mmIdsCache.remove(productOffers.mmId)
                logger.info("Sending DELETED event with mmId: ${productOffers.mmId}")
            }
        }

        return producerRecord
    }
    private fun createProducerRecord(productOffers: ProductOffers, header: Header) = ProducerRecord(
        ProductOffersStreamsConfig.PRODUCT_OFFER_FAT_EVENT_TOPIC,
        productOffers.mmId,
        productOffers
    ).apply { this.headers().add(header) }
}

enum class HeaderType { CREATED, UPDATED, DELETED }