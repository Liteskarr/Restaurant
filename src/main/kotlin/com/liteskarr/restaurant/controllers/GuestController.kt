package com.liteskarr.restaurant.controllers

import com.liteskarr.restaurant.dto.DishReviewDto
import com.liteskarr.restaurant.dto.MenuDishDto
import com.liteskarr.restaurant.models.*
import com.liteskarr.restaurant.services.DishReviewService
import com.liteskarr.restaurant.services.DishService
import com.liteskarr.restaurant.services.OrderService
import com.liteskarr.restaurant.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.ModelAndView
import java.security.Principal
import kotlin.math.ceil

@Controller
@RequestMapping("/guest")
class GuestController {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var dishService: DishService

    @Autowired
    private lateinit var dishReviewService: DishReviewService

    @Autowired
    private lateinit var orderService: OrderService

    private val errorByUser = HashMap<Long, String>()

    private val logger = LoggerFactory.getLogger(GuestController::class.java)

    @GetMapping
    fun index(model: Model, principal: Principal?): ModelAndView {
        try {
            val (user, order) = getUserAndOrderFromPrinciple(principal)
            val dishes = dishService.retrieveAllAvailable().map { convertDishToMenuDto(it, it.count) }
            val orderedDishes = (order.dishes).map { convertDishToMenuDto(it) }
            model.addAttribute("orderState", order.state.name)
            model.addAttribute("dishes", dishes)
            model.addAttribute("orderedDishes", orderedDishes)
            model.addAttribute("costOfOrder", order.cost)
            if (errorByUser[user.id] != null) {
                model.addAttribute("error", errorByUser[user.id])
                errorByUser.remove(user.id)
            }
            model.addAttribute("progress", orderService.getProgressForOrder(order))
            return ModelAndView("guest-order")
        } catch (e: Exception) {
            logger.error(e.message)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PostMapping
    fun postIndex(@ModelAttribute("operation") operation: String, principal: Principal?): String {
        var user: User? = null
        try {
            val (u, order) = getUserAndOrderFromPrinciple(principal)
            user = u
            when (operation) {
                "update" -> {}
                "make" -> orderService.takeOrder(order)
                "cancel" -> orderService.cancelOrder(order)
                "pay" -> {
                    return "redirect:/guest/pay"
                }

                else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Undefined operation!")
            }
        } catch (e: Exception) {
            if (user?.id != null) {
                errorByUser[user.id!!] = e.message ?: ""
            }
        }
        return "redirect:/guest"
    }

    @PostMapping("/add/{id}")
    fun addInOrder(@PathVariable("id") id: Long, principal: Principal?): String {
        var user: User? = null
        try {
            val (u, order) = getUserAndOrderFromPrinciple(principal)
            user = u
            val dish = dishService.retrieveById(id)
            dish ?: throw Exception("No Dish with received ID!")
            orderService.addDishInOrder(order, dish)
        } catch (e: Exception) {
            if (user?.id != null) {
                errorByUser[user.id!!] = e.message ?: ""
            }
        }
        return "redirect:/guest"
    }

    @PostMapping("/delete/{id}")
    fun deleteFromOrder(@PathVariable("id") id: Long, principal: Principal?): String {
        var user: User? = null
        try {
            val (u, order) = getUserAndOrderFromPrinciple(principal)
            user = u
            val dish = dishService.retrieveById(id)
            dish ?: throw Exception("No Dish with received ID!")
            orderService.removeDishFromOrder(order, dish)
        } catch (e: Exception) {
            if (user?.id != null) {
                errorByUser[user.id!!] = e.message ?: ""
            }
        }
        return "redirect:/guest"
    }

    @GetMapping("/pay")
    fun pay(model: Model, principal: Principal?): ModelAndView {
        try {
            val (_, order) = getUserAndOrderFromPrinciple(principal)
            model.addAttribute("cost", "Cost of order: ${order.cost} \$")
            model.addAttribute("id", order.id)
            return ModelAndView("guest-pay")
        } catch (e: Exception) {
            logger.error(e.message)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PostMapping("/pay/{id}")
    fun postPay(@PathVariable("id") id: Long, principal: Principal?): String {
        try {
            val (_, order) = getUserAndOrderFromPrinciple(principal)
            orderService.payForOrder(order)
            return "redirect:/guest/review/$id"
        } catch (e: Exception) {
            logger.error(e.message)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @GetMapping("/review/{id}")
    fun review(@PathVariable("id") id: Long, model: Model, principal: Principal?): ModelAndView {
        try {
            val order = orderService.retrieveById(id)
            val dishes = order.dishes.distinctBy { it.id }
            model.addAttribute("id", id)
            model.addAttribute("dishes", dishes)
            model.addAttribute("review", DishReviewDto(0, ""))
            model.addAttribute("operation")
            model.addAttribute("dishId")
            return ModelAndView("guest-review")
        } catch (e: Exception) {
            logger.error(e.message)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PostMapping("/review/{id}")
    fun postReview(
        @PathVariable("id") id: Long,
        @ModelAttribute("dishId") dishId: Long?,
        @ModelAttribute("operation") operation: String?,
        @ModelAttribute("review") review: DishReviewDto,
        principal: Principal?
    ): String {
        try {
            if (operation != "send") {
                return "redirect:/guest"
            }
            val order = orderService.retrieveById(id)
            dishId ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)
            if (review.rate != 0) {
                val dish = dishService.retrieveById(dishId) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)
                dishReviewService.createReview(dish, order, review.rate, review.text)
            }
            return "redirect:/guest/review/$id"
        } catch (e: Exception) {
            logger.error(e.message)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    private fun convertDishToMenuDto(dish: Dish, count: Int? = null) = MenuDishDto(
        dish.id!!, "\"${dish.name}\" (${dish.cost}$, ~${ceil(dish.timeToReady.inWholeSeconds / 60.0).toLong()} minute)${
            if (count != null) {
                " x${count}"
            } else {
                ""
            }
        }"
    )

    private fun getUserAndOrderFromPrinciple(principal: Principal?): Pair<User, Order> {
        val user = userService.retrieveUserByName(principal?.name)
        user ?: throw Exception("User not found!")
        val order = (orderService.findOpenedOrderForUser(user) ?: orderService.createOrder(user))
        if (order.state.isEqual(OrderState.IN_PROGRESS) && !orderService.isStarted(order)) {
            orderService.takeOrder(order)
        }
        return Pair(user, order)
    }
}