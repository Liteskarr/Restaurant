package com.liteskarr.restaurant.models

import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
@Table(name = "restaurant_users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    @Size(min = 4)
    var name: String = "",

    @Column(nullable = false)
    private val password: String = "",

    @ManyToOne(fetch = FetchType.EAGER)
    var role: Role = Role.GUEST,
) {
    fun retrievePassword() = password
}