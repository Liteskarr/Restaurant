package com.liteskarr.restaurant.controllers

import com.liteskarr.restaurant.models.Dish
import com.liteskarr.restaurant.services.DishService
import com.liteskarr.restaurant.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import kotlin.time.Duration.Companion.seconds

@Controller
@RequestMapping("/test")
class TestController {
    @Autowired
    lateinit var dishService: DishService

    @Autowired
    lateinit var userService: UserService

    @GetMapping
    fun index(): String = try {
        if (userService.retrieveUserByName("admin") == null) {
            userService.registerAdmin("admin", "admin")
        }

        dishService.create(Dish(null, "Hedgehog with octopus and tomato", 12, 10, 20.seconds))
        dishService.create(Dish(null, "Tasty milk with corn", 8, 12, 25.seconds))
        dishService.create(Dish(null, "Blin", 4, 7, 15.seconds))
        "redirect:/login"
    } catch (e: Exception) {
        e.message ?: ""
    }
}