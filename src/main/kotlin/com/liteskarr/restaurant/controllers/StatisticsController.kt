package com.liteskarr.restaurant.controllers

import com.liteskarr.restaurant.dto.StatisticsDto
import com.liteskarr.restaurant.models.OrderState
import com.liteskarr.restaurant.services.DishReviewService
import com.liteskarr.restaurant.services.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/statistics")
class StatisticsController {
    @Autowired
    private lateinit var dishReviewService: DishReviewService

    @Autowired
    private lateinit var orderService: OrderService

    @GetMapping
    fun getStatistics(): ResponseEntity<StatisticsDto> = try {
        ResponseEntity.ok(
            StatisticsDto(
                revenue = orderService.retrieveAll().filter { it.state.isEqual(OrderState.CLOSED) }.map { it.cost }
                    .reduceOrNull { acc: Int, i: Int -> acc + i } ?: 0,
                mostPopularDishes = dishReviewService.retrieveMostPopularDishes(),
                averageRateOfDishes = dishReviewService.getAverageRateOfDished()
            )
        )
    } catch (e: Exception) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
    }
}