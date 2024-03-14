package com.liteskarr.restaurant.repositories

import com.liteskarr.restaurant.models.Dish
import org.springframework.data.jpa.repository.JpaRepository

interface DishRepository : JpaRepository<Dish, Long>