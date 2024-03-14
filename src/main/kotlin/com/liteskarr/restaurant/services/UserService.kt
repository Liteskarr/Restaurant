package com.liteskarr.restaurant.services

import com.liteskarr.restaurant.models.Role
import com.liteskarr.restaurant.models.User
import com.liteskarr.restaurant.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    fun retrieveUserByName(name: String?): User? = userRepository.findUserByName(name)

    fun retrieveAll(): List<User> = userRepository.findAll()

    fun registerAdmin(name: String, password: String) {
        try {
            val user = User(
                name = name,
                password = passwordEncoder.encode(password),
                role = Role.ADMIN
            )
            userRepository.save(user)
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    fun register(name: String, password: String) {
        try {
            val user = User(
                name = name,
                password = passwordEncoder.encode(password),
                role = Role.GUEST
            )
            userRepository.save(user)
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

}