package com.liteskarr.restaurant.models

import jakarta.persistence.*

@Entity
@Table(name = "restaurant_roles")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val name: String = ""
) {
    companion object {
        val ADMIN = Role(1, "ADMIN")
        val GUEST = Role(2, "GUEST")
    }

    fun isEqual(other: Role) = name == other.name
}