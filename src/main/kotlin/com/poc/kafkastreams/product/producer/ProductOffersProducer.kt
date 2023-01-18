package com.poc.kafkastreams.product.producer

import com.poc.kafkastreams.product.configuration.ProductOffersStreamsConfig
import com.poc.kafkastreams.product.model.ProductOffers
import com.poc.kafkastreams.product.model.ProductOffersFactory
import io.github.serpro69.kfaker.Faker
import org.apache.kafka.clients.producer.ProducerRecord
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
    @Qualifier("product-offers")private val kafkaTemplate: KafkaTemplate<Int, ProductOffers>
) {
    companion object {
        private const val MM_EVENT_STATE_HEADER = "mm-event-state"
    }

    @EventListener(ApplicationStartedEvent::class)
    operator fun invoke(): Flux<CompletableFuture<SendResult<Int, ProductOffers>>> = Flux.zip(
        Flux.interval(1000.milliseconds.toJavaDuration()),
        Flux.fromStream(Stream.generate { createProducerRecord() })
    ).map { kafkaTemplate.send(it.t2) }

    private fun createProducerRecord(): ProducerRecord<Int, ProductOffers> = ProducerRecord(
        ProductOffersStreamsConfig.PRODUCT_OFFER_FAT_EVENT_TOPIC,
        Faker().random.nextLong().toInt(),
        ProductOffersFactory.withRandomCategories()
    )
        .apply { this.headers().add(MM_EVENT_STATE_HEADER, HeaderType.CREATED.toString().toByteArray()) }
}

enum class HeaderType { CREATED, UPDATED }