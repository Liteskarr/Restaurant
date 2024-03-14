package com.liteskarr.restaurant.adapters

import com.liteskarr.restaurant.models.Role
import org.springframework.security.core.GrantedAuthority

class GrantedAuthorityAdapter(private val role: Role) : GrantedAuthority {
    override fun getAuthority(): String {
        return role.name
    }
}