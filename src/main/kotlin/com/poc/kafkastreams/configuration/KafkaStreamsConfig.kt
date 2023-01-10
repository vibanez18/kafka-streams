package com.poc.kafkastreams.configuration

import com.poc.kafkastreams.processor.FatEventProcessor
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration
import org.springframework.kafka.config.KafkaStreamsConfiguration

@Configuration
@EnableKafkaStreams
@EnableKafka
class KafkaStreamsConfig(
    @Value(value = "\${spring.kafka.bootstrap-servers}") private val bootstrapAddress: String
) {
    companion object {
        const val numPartition = 3
        const val replicationFactor: Short = 1
        const val COUNT_WORDS_TOPIC = "count-words"
        const val FAT_EVENTS_TOPIC = "fat_events"
    }

    @Bean(name = [KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME])
    fun kStreamsConfig(): KafkaStreamsConfiguration {
        val props: MutableMap<String, Any?> = HashMap()
        props[StreamsConfig.APPLICATION_ID_CONFIG] = "streams-app"
        props[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
        props[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.Long().javaClass.name
        return KafkaStreamsConfiguration(props)
    }

    @Bean
    fun fatEvents(): NewTopic = NewTopic(FAT_EVENTS_TOPIC, numPartition, replicationFactor)

    @Bean
    fun counts(): NewTopic = NewTopic(COUNT_WORDS_TOPIC, numPartition, replicationFactor)

    @Bean
    fun fatEventProcessor(streamsBuilder: StreamsBuilder) =
        FatEventProcessor()(streamsBuilder, FAT_EVENTS_TOPIC, COUNT_WORDS_TOPIC)
}
