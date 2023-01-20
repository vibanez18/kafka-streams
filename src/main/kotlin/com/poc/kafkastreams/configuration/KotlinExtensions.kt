package com.poc.kafkastreams.configuration

import io.github.serpro69.kfaker.Faker
import kotlin.random.Random

fun Long.Companion.randomLong(): Long = Random.nextLong(0, 10)
fun Long.Companion.randomLongs(): List<Long> =
    listOf(
        this.randomLong(),
        this.randomLong(),
        this.randomLong(),
        this.randomLong(),
        this.randomLong(),
        this.randomLong()
    )
fun String.Companion.randomUUID(): String = Faker().random.nextUUID()