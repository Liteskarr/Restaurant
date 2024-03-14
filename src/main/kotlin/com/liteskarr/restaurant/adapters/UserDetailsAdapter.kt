package com.liteskarr.restaurant.adapters

import com.liteskarr.restaurant.models.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsAdapter(private val user: User) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(
            GrantedAuthorityAdapter(user.role)
        )
    }

    override fun getPassword(): String = user.retrievePassword()

    override fun getUsername(): String = user.name

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}