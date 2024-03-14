package com.liteskarr.restaurant.dto

data class AdminDishReviewDto(
    val orderId: Long,
    val dishId: Long,
    val dish: String,
    val rate: Int,
    val text: String
)