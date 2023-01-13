package com.poc.kafkastreams.configuration

import com.poc.kafkastreams.processor.FatEventCountWordsProcessor
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
@Profile("hobbit")
class HobbitStreamsConfiguration {

    @Bean
    fun counts(): NewTopic = NewTopic(KafkaStreamsConfig.COUNT_WORDS_TOPIC, KafkaStreamsConfig.numPartition, KafkaStreamsConfig.replicationFactor)


    @Bean("hobbitStreamBuilder")
    fun myKStreamBuilder(streamsConfig: KafkaStreamsConfiguration): FactoryBean<StreamsBuilder> =
        StreamsBuilderFactoryBean(streamsConfig)

    @Bean
    fun fatEventProcessor(@Qualifier("hobbitStreamBuilder") streamsBuilder: StreamsBuilder) =
        FatEventCountWordsProcessor()(streamsBuilder,
            KafkaStreamsConfig.FAT_EVENTS_TOPIC,
            KafkaStreamsConfig.COUNT_WORDS_TOPIC
        )
}