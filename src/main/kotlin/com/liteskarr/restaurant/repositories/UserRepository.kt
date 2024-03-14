package com.liteskarr.restaurant.repositories

import com.liteskarr.restaurant.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findUserByName(username: String?): User?
}