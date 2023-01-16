package com.poc.kafkastreams.hobbit.consumer

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@Profile("hobbit")
class CountWordsConsumer: Logging {

    @KafkaListener(topics = ["count-words"], groupId = "consumer-streams-app")
    operator fun invoke(record: ConsumerRecord<String, Long>) =
        logger.info(("received = " + record.value() + " with key " + record.key()))
}
