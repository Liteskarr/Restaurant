package com.liteskarr.restaurant.repositories

import com.liteskarr.restaurant.models.DishReview
import org.springframework.data.jpa.repository.JpaRepository

interface DishReviewRepository : JpaRepository<DishReview, Long>