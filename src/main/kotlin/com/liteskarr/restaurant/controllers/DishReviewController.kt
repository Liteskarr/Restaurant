package com.liteskarr.restaurant.controllers

import com.liteskarr.restaurant.dto.AdminDishReviewDto
import com.liteskarr.restaurant.services.DishReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reviews")
class DishReviewController {
    @Autowired
    private lateinit var dishReviewService: DishReviewService

    @GetMapping("/all")
    fun getAll(): ResponseEntity<List<AdminDishReviewDto>> =
        ResponseEntity.ok(dishReviewService.retrieveAll().map {
            AdminDishReviewDto(
                orderId = it.order!!.id!!,
                dishId = it.dish!!.id!!,
                dish = it.dish!!.name,
                rate = it.rate!!,
                text = it.text!!
            )
        })
}