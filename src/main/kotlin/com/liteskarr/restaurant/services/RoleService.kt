package com.liteskarr.restaurant.services

import com.liteskarr.restaurant.models.Role
import com.liteskarr.restaurant.repositories.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoleService(
    @Autowired
    private var roleRepository: RoleRepository
) {
    init {
        roleRepository.save(Role.ADMIN)
        roleRepository.save(Role.GUEST)
    }
}