package com.liteskarr.restaurant.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero
import lombok.NoArgsConstructor
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Entity
@Table(name = "dishes")
@NoArgsConstructor
data class Dish(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    @NotBlank
    var name: String = "",

    @Column(nullable = false)
    @PositiveOrZero
    var cost: Int = 0,

    @Column(nullable = false)
    @PositiveOrZero
    var count: Int = 0,

    @Column(nullable = false)
    @PositiveOrZero
    var timeToReady: Duration = 0.seconds
) {
    constructor() : this(null)
}