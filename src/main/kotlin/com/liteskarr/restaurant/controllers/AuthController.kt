@file:Suppress("SameReturnValue", "SameReturnValue")

package com.liteskarr.restaurant.controllers

import com.liteskarr.restaurant.dto.AuthRequestDto
import com.liteskarr.restaurant.models.Role
import com.liteskarr.restaurant.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import java.security.Principal


@Suppress("SameReturnValue", "SameReturnValue")
@Controller
class AuthController {
    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/")
    fun index(@Autowired principal: Principal?): String {
        val user = userService.retrieveUserByName(principal?.name)
        user ?: return "redirect:/login"
        return if (user.role.isEqual(Role.ADMIN)) {
            "redirect:/swagger-ui.html"
        } else if (user.role.isEqual(Role.GUEST)) {
            "redirect:/guest"
        } else {
            throw Exception("Undefined role!")
        }
    }

    @GetMapping("/register")
    fun registerPage(model: Model): String {
        model.addAttribute("registerRequest", AuthRequestDto("", ""))
        return "guest-register"
    }

    @PostMapping("/register")
    fun registerForm(@ModelAttribute("registerRequest") registerRequest: AuthRequestDto): String {
        userService.register(registerRequest.name, registerRequest.password)
        return "redirect:/login"
    }
}