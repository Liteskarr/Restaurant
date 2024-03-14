package com.liteskarr.restaurant.adapters

import com.liteskarr.restaurant.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceAdapter : UserDetailsService {
    @Autowired
    private lateinit var userService: UserService

    override fun loadUserByUsername(name: String?): UserDetails {
        val user = userService.retrieveUserByName(name)
        user ?: throw Exception("No user with such name!")
        return UserDetailsAdapter(user)
    }
}