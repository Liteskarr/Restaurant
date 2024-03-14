package com.liteskarr.restaurant.controllers

import com.liteskarr.restaurant.models.Dish
import com.liteskarr.restaurant.services.DishService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/api/dishes")
class DishController {
    @Autowired
    private lateinit var dishService: DishService

    @GetMapping("/menu")
    fun getActualMenu(): ResponseEntity<List<Dish>> = try {
        ResponseEntity.ok(dishService.retrieveAll())
    } catch (e: Exception) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
    }

    @GetMapping("/{id}")
    fun getDish(@PathVariable("id") id: Long): ResponseEntity<Dish> = try {
        ResponseEntity.ok(dishService.retrieveById(id))
    } catch (e: Exception) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    fun postDish(@RequestBody dish: Dish): ResponseEntity<Dish> = try {
        ResponseEntity.ok(dishService.create(dish))
    } catch (e: Exception) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    fun updateDish(@RequestBody dish: Dish): ResponseEntity<Dish> = try {
        ResponseEntity.ok(dishService.update(dish))
    } catch (e: Exception) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    fun deleteDish(@PathVariable("id") id: Long): ResponseEntity<Long> = try {
        dishService.delete(id)
        ResponseEntity.ok(id)
    } catch (e: Exception) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
    }
}