package com.liteskarr.restaurant.services

import com.liteskarr.restaurant.models.Dish
import com.liteskarr.restaurant.repositories.DishRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DishService {
    @Autowired
    private lateinit var dishRepository: DishRepository

    fun retrieveAll(): List<Dish> {
        return dishRepository.findAll()
    }

    fun retrieveAllAvailable(): List<Dish> {
        return dishRepository.findAll().filter { it.count > 0 }.sortedBy { it.name }
    }

    fun retrieveById(id: Long): Dish? {
        return dishRepository.getReferenceById(id)
    }

    fun create(dish: Dish): Dish {
        if (dish.id != null) throw Exception("Id of Dish must be null!")
        return dishRepository.save(dish)
    }

    fun update(dish: Dish): Dish {
        dish.id ?: throw Exception("Id of Dish mustn't be null!")
        return dishRepository.save(dish)
    }

    fun delete(id: Long) {
        dishRepository.deleteById(id)
    }

    fun addCount(dish: Dish, count: Int = 1) {
        dish.count += count
        dishRepository.save(dish)
    }

    fun reduceCount(dish: Dish, count: Int = 1) {
        if (dish.count < count) {
            throw Exception("Count of Dish less than you want reduce!")
        }
        dish.count -= count
        dishRepository.save(dish)
    }
}