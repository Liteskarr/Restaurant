package com.liteskarr.restaurant.models

import jakarta.persistence.*

@Entity
@Table(name = "order_states")
class OrderState (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val name: String = ""
) {
    companion object {
        val OPENED = OrderState(1, "OPENED")
        val IN_PROGRESS = OrderState(2, "IN_PROGRESS")
        val DONE = OrderState(3, "DONE")
        val CLOSED = OrderState(4, "CLOSED")
        val CANCELLED = OrderState(5, "CANCELLED")
    }

    fun isEqual(other: OrderState) = name == other.name
}