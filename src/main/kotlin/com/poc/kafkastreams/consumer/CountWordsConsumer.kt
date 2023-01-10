package com.poc.kafkastreams.consumer

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class CountWordsConsumer: Logging {

    @KafkaListener(topics = ["count-words"], groupId = "consumer-streams-app")
    operator fun invoke(record: ConsumerRecord<String, Long>) =
        logger.info(("received = " + record.value() + " with key " + record.key()))
}
