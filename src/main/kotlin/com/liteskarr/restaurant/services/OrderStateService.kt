package com.liteskarr.restaurant.services

import com.liteskarr.restaurant.models.OrderState
import com.liteskarr.restaurant.repositories.OrderStateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrderStateService(
    @Autowired
    private val orderStateRepository: OrderStateRepository
) {
    init {
        orderStateRepository.save(OrderState.OPENED)
        orderStateRepository.save(OrderState.IN_PROGRESS)
        orderStateRepository.save(OrderState.DONE)
        orderStateRepository.save(OrderState.CLOSED)
        orderStateRepository.save(OrderState.CANCELLED)
    }
}