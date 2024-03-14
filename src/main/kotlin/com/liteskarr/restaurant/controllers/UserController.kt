package com.liteskarr.restaurant.controllers

import com.liteskarr.restaurant.dto.AuthRequestDto
import com.liteskarr.restaurant.dto.AuthResponseDto
import com.liteskarr.restaurant.models.User
import com.liteskarr.restaurant.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/users")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/all")
    fun getAll() = userService.retrieveAll()

    @GetMapping("/{name}")
    fun getByName(@PathVariable("name") name: String): ResponseEntity<User> {
        try {
            val dish = userService.retrieveUserByName(name)
            return if (dish == null) {
                ResponseEntity.status(400).build()
            } else {
                ResponseEntity.ok(dish)
            }
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: AuthRequestDto): ResponseEntity<AuthResponseDto> {
        try {
            userService.register(registerRequest.name, registerRequest.password)
            return ResponseEntity.ok(AuthResponseDto(registerRequest.name))
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}