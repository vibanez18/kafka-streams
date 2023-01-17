package com.poc.kafkastreams.product.consumer

import com.poc.kafkastreams.product.model.ProductOffers
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@Profile("product-offers")
class ProductOfferConsumer: Logging {
    @KafkaListener(topics = ["categories_attributes_topic"], groupId = "consumer-pr-offers-streams-app")
    operator fun invoke(record: ConsumerRecord<Int, ProductOffers>) =
        logger.info("received = ${record.value()} with key ${record.key()}")
}