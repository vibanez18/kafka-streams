package com.poc.kafkastreams.configuration

import com.poc.kafkastreams.processor.FatEventProcessor
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.streams.StreamsBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafkaStreams

@Configuration
@EnableKafkaStreams
//@EnableKafka
class KafkaStreamsConfiguration {
    companion object {
        const val numPartition = 3
        const val replicationFactor: Short = 1
        const val COUNT_WORDS_TOPIC = "count-words"
        const val FAT_EVENTS_TOPIC = "fat_events"
    }

    @Bean
    fun fatEvents(): NewTopic = NewTopic(FAT_EVENTS_TOPIC, numPartition, replicationFactor)

    @Bean
    fun counts(): NewTopic = NewTopic(COUNT_WORDS_TOPIC, numPartition, replicationFactor)

    @Bean
    fun fatEventProcessor(streamsBuilder: StreamsBuilder) =
        FatEventProcessor()(streamsBuilder, FAT_EVENTS_TOPIC, COUNT_WORDS_TOPIC)
}