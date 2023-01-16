package com.poc.kafkastreams.configuration

import io.github.serpro69.kfaker.Faker

fun Long.Companion.randomLong(): Long = Faker().random.nextLong()
fun Long.Companion.randomLongs(): List<Long> =
    listOf(
        this.randomLong(),
        this.randomLong(),
        this.randomLong(),
        this.randomLong(),
        this.randomLong(),
        this.randomLong()
    )