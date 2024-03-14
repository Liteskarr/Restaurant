package com.liteskarr.restaurant.repositories

import com.liteskarr.restaurant.models.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long>
