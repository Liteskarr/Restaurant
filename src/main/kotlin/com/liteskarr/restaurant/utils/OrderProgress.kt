package com.liteskarr.restaurant.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.DurationUnit

class OrderProgress {
    private var start: Instant = Clock.System.now()
    private var duration: Duration = Duration.ZERO

    fun start() {
        start = Clock.System.now()
    }

    fun add(duration: Duration) {
        this.duration += duration
    }

    fun getProgress(): Int =
        min(
            ((Clock.System.now() - start).toDouble(DurationUnit.SECONDS) / duration.toDouble(DurationUnit.SECONDS) * 100.0).toInt(),
            100
        )

    fun shouldWait(): Boolean {
        return Clock.System.now() < (start + duration)
    }
}