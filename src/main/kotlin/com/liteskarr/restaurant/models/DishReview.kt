package com.liteskarr.restaurant.models

import jakarta.persistence.*
import org.hibernate.validator.constraints.Range

@Entity
@Table(name = "dish_reviews")
class DishReview(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,

    @ManyToOne
    var dish: Dish? = null,

    @ManyToOne
    var order: Order? = null,

    @Column(nullable = true)
    @Range(min = 1, max = 5)
    var rate: Int? = null,

    @Column(nullable = true)
    var text: String? = null
)