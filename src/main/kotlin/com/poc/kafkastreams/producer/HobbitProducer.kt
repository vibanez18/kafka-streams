package com.poc.kafkastreams.producer

import io.github.serpro69.kfaker.Faker
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.stream.Stream
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

@Component
class HobbitProducer(
    private val kafkaTemplate: KafkaTemplate<Int, String>
) {

    @EventListener(ApplicationStartedEvent::class)
    operator fun invoke() {
        val faker = Faker()
        val interval: Flux<Long> = Flux.interval(1000.milliseconds.toJavaDuration())
        val quotes: Flux<String> = Flux.fromStream(Stream.generate { faker.hobbit.quote() })

        Flux.zip(interval, quotes)
            .map { kafkaTemplate.send(
                    "fat_events",
                    faker.random.nextInt(),
                    it.t2
                )
            }.blockLast()
    }
}
