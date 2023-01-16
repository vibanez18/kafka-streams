package com.poc.kafkastreams.product.producer

import com.poc.kafkastreams.product.configuration.ProductOffersStreamsConfig
import com.poc.kafkastreams.product.model.ProductOffers
import io.github.serpro69.kfaker.Faker
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.stream.Stream
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

@Component
@Profile("product-offers")
class ProductOffersProducer(
    private val kafkaTemplate: KafkaTemplate<Int, ProductOffers>
) {
    @EventListener(ApplicationStartedEvent::class)
    operator fun invoke() =
        Flux.zip(
            Flux.interval(1000.milliseconds.toJavaDuration()),
            Flux.fromStream(Stream.generate { ProductOffersFactory.withRandomCategories() })
        ).map {
            kafkaTemplate.send(
                ProductOffersStreamsConfig.PRODUCT_OFFER_FAT_EVENT_TOPIC,
                Faker().random.nextInt(),
                it.t2
            )
        }
}