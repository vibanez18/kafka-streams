package com.poc.kafkastreams.product.configuration

import com.poc.kafkastreams.product.model.ProductOffers
import com.poc.kafkastreams.product.topologies.ExtractorProductOffersFunction
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.IntegerDeserializer
import org.apache.kafka.common.serialization.IntegerSerializer
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer


@Configuration
@Profile("product-offers")
class ProductOffersStreamsConfig(
    @Value(value = "\${spring.kafka.bootstrap-servers}") private val bootstrapAddress: String

) {
    companion object {
        const val NUM_PARTITION = 3
        const val REPLICATION_FACTOR: Short = 1
        const val CATEGORIES_ATTRIBUTES_TOPIC = "categories_attributes_topic"
        const val PRODUCT_OFFER_FAT_EVENT_TOPIC = "product_offer_fat_event_topic"
        const val COMMIT_INTERVAL_KTABLE = 1000
        const val KTABLE_APP_ID = "ktable-id"
    }

    @Bean
    fun productOfferFatEventTopic(): NewTopic =
        NewTopic(PRODUCT_OFFER_FAT_EVENT_TOPIC, NUM_PARTITION, REPLICATION_FACTOR)

    @Bean
    fun categoriesAttributesTopic(): NewTopic =
        NewTopic(CATEGORIES_ATTRIBUTES_TOPIC, NUM_PARTITION, REPLICATION_FACTOR)

    @Bean
    fun productOffersProducerFactory(): ProducerFactory<Int, ProductOffers> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = IntegerSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean("product-offers")
    fun productOffersKafkaTemplate(): KafkaTemplate<Int, ProductOffers> {
        return KafkaTemplate(productOffersProducerFactory())
    }

    @Bean
    fun  productOffersConsumerFactory(): ConsumerFactory<Int, ProductOffers> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress

        return DefaultKafkaConsumerFactory(props, IntegerDeserializer(), JsonDeserializer(ProductOffers::class.java))
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<Int, ProductOffers> {
        val factory = ConcurrentKafkaListenerContainerFactory<Int, ProductOffers>()
        factory.consumerFactory = productOffersConsumerFactory()
        return factory
    }

    @Bean("productOffersKTableBuilder")
    fun productOffersKTableBuilder() = StreamsBuilderFactoryBean(
        KafkaStreamsConfiguration(
            mapOf<String, Any>(
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
                StreamsConfig.APPLICATION_ID_CONFIG to KTABLE_APP_ID,
                StreamsConfig.COMMIT_INTERVAL_MS_CONFIG to COMMIT_INTERVAL_KTABLE
            )
        )
    )


    @Bean
    fun productOffersKTable(@Qualifier("productOffersKTableBuilder") streamsBuilder: StreamsBuilder) =
        ExtractorProductOffersFunction()(streamsBuilder, PRODUCT_OFFER_FAT_EVENT_TOPIC, CATEGORIES_ATTRIBUTES_TOPIC)
}
