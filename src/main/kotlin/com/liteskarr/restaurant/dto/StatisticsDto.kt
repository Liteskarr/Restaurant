package com.liteskarr.restaurant.dto

data class StatisticsDto(
    val revenue: Int,
    val mostPopularDishes: List<String>,
    val averageRateOfDishes: Map<String, Double>,
)
