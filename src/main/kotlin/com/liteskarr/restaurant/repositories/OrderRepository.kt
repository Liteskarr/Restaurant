package com.liteskarr.restaurant.repositories

import com.liteskarr.restaurant.models.Order
import com.liteskarr.restaurant.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {
    fun findOrdersByGuest(user: User): List<Order>
}