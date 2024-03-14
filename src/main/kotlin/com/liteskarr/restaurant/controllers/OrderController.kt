package com.liteskarr.restaurant.controllers

import com.liteskarr.restaurant.models.Order
import com.liteskarr.restaurant.services.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/orders")
class OrderController {
    @Autowired
    private lateinit var orderService: OrderService

    @GetMapping("/all")
    fun getAll(): ResponseEntity<List<Order>> = ResponseEntity.ok(orderService.retrieveAll())
}