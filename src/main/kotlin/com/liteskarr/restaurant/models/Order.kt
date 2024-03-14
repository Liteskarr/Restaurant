package com.liteskarr.restaurant.models

import jakarta.persistence.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,

    @ManyToOne
    var guest: User? = null,

    @ManyToMany(targetEntity = Dish::class, fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
    var dishes: MutableList<Dish> = mutableListOf(),

    @ManyToOne
    var state: OrderState = OrderState.OPENED
) {
    val cost: Int
        get() = dishes.map { it.cost }.reduceOrNull { acc, i -> acc + i } ?: 0

    val timeToReady: Duration
        get() = dishes.maxOfOrNull { it.timeToReady } ?: 0.seconds
}