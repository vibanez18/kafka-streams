package com.poc.kafkastreams

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaStreamsApplication

fun main(args: Array<String>) {
	runApplication<KafkaStreamsApplication>(*args)
}
