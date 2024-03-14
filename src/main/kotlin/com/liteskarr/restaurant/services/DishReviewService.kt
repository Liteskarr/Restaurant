package com.liteskarr.restaurant.services

import com.liteskarr.restaurant.models.Dish
import com.liteskarr.restaurant.models.DishReview
import com.liteskarr.restaurant.models.Order
import com.liteskarr.restaurant.repositories.DishReviewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DishReviewService {
    @Autowired
    private lateinit var dishReviewRepository: DishReviewRepository

    @Autowired
    private lateinit var orderService: OrderService

    fun retrieveAll(): List<DishReview> = dishReviewRepository.findAll()

    fun createReview(dish: Dish, order: Order, rate: Int, text: String) {
        dishReviewRepository.save(DishReview(null, dish, order, rate, text))
    }

    fun retrieveMostPopularDishes(): List<String> {
        val dishes =
            orderService.retrieveAll().map { it.dishes }.reduceOrNull { acc, dishes -> (acc + dishes).toMutableList() }
                ?: mutableListOf()
        val map = HashMap<String, Int>()
        for (dish in dishes) {
            if (!map.contains(dish.name)) {
                map[dish.name] = dishes.count { it.name == dish.name }
            }
        }
        val stat = map.maxBy { it.value }.value
        return map.filter { it.value == stat }.map { it.key }.toList()
    }

    fun getAverageRateOfDished(): Map<String, Double> {
        val reviews = dishReviewRepository.findAll()
        val map = HashMap<String, MutableList<Double>>()
        for (review in reviews) {
            if (!map.contains(review.dish!!.name)) {
                map[review.dish!!.name] = mutableListOf(review.rate!!.toDouble())
            } else {
                map[review.dish!!.name]!!.add(review.rate!!.toDouble())
            }
        }
        return map.map { it.key to it.value.average() }.toMap()
    }
}