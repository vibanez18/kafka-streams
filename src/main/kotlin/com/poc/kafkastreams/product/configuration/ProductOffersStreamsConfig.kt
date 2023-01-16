package com.poc.kafkastreams.product.configuration

import com.poc.kafkastreams.product.processor.ProductOfferProcessor
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.streams.StreamsBuilder
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.kafka.config.StreamsBuilderFactoryBean

@Configuration
@Profile("product-offers")
class ProductOffersStreamsConfig {

    companion object {
        const val NUM_PARTITION = 3
        const val REPLICATION_FACTOR: Short = 1
        const val CATEGORIES_ATTRIBUTES_TOPIC = "categories_attributes_topic"
        const val PRODUCT_OFFER_FAT_EVENT_TOPIC = "product_offer_fat_event_topic"
    }

    @Bean
    fun productOfferFatEventTopic(): NewTopic = NewTopic(PRODUCT_OFFER_FAT_EVENT_TOPIC, NUM_PARTITION, REPLICATION_FACTOR)

    @Bean
    fun categoriesAttributesTopic(): NewTopic = NewTopic(CATEGORIES_ATTRIBUTES_TOPIC, NUM_PARTITION, REPLICATION_FACTOR)

    @Bean("productOffersBuilder")
    fun productOffersBuilder(streamsConfig: KafkaStreamsConfiguration): FactoryBean<StreamsBuilder> =
        StreamsBuilderFactoryBean(streamsConfig)

    @Bean
    fun fatEventProcessor(@Qualifier("productOffersBuilder") streamsBuilder: StreamsBuilder) =
        ProductOfferProcessor()(streamsBuilder,
            PRODUCT_OFFER_FAT_EVENT_TOPIC,
            CATEGORIES_ATTRIBUTES_TOPIC
        )
}