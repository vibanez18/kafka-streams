package com.poc.kafkastreams.hobbit.configuration

import com.poc.kafkastreams.hobbit.processor.CountWordsProcessor
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

    companion object {
        const val NUM_PARTITION = 3
        const val REPLICATION_FACTOR: Short = 1
        const val COUNT_WORDS_TOPIC = "count-words"
        const val HOBBIT_QUOTES_TOPIC = "hobbit_quotes"
    }

    @Bean
    fun counts(): NewTopic = NewTopic(COUNT_WORDS_TOPIC, NUM_PARTITION, REPLICATION_FACTOR)

    @Bean
    fun hobbitInputTopic(): NewTopic = NewTopic(
        HOBBIT_QUOTES_TOPIC,
        NUM_PARTITION,
        REPLICATION_FACTOR
    )

    @Bean("hobbitStreamBuilder")
    fun hobbitStreamBuilder(streamsConfig: KafkaStreamsConfiguration): FactoryBean<StreamsBuilder> =
        StreamsBuilderFactoryBean(streamsConfig)

    @Bean
    fun fatEventProcessor(@Qualifier("hobbitStreamBuilder") streamsBuilder: StreamsBuilder) =
        CountWordsProcessor()(streamsBuilder,
            HOBBIT_QUOTES_TOPIC,
            COUNT_WORDS_TOPIC
        )
}