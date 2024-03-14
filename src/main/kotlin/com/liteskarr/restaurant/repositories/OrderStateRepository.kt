package com.liteskarr.restaurant.repositories

import com.liteskarr.restaurant.models.OrderState
import org.springframework.data.jpa.repository.JpaRepository

interface OrderStateRepository : JpaRepository<OrderState, Long>