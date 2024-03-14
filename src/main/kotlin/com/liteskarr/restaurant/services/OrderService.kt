package com.liteskarr.restaurant.services

import com.liteskarr.restaurant.models.Dish
import com.liteskarr.restaurant.models.Order
import com.liteskarr.restaurant.models.OrderState
import com.liteskarr.restaurant.models.User
import com.liteskarr.restaurant.repositories.OrderRepository
import com.liteskarr.restaurant.utils.OrderProgress
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

@Service
class OrderService {
    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var dishService: DishService

    private val ordersProgress = ConcurrentHashMap<Long, OrderProgress>()
    private val readyOrders = ConcurrentLinkedQueue<Long>()

    @Scheduled(fixedRate = 1000)
    fun checkReadyOrders() {
        while (!readyOrders.isEmpty()) {
            val id = readyOrders.remove()
            var order: Order
            try {
                order = orderRepository.getReferenceById(id)
                if (order.state.isEqual(OrderState.IN_PROGRESS)) {
                    order.state = OrderState.DONE
                } else {
                    continue
                }
            } catch (e: Exception) {
                continue
            }
            orderRepository.save(order)
        }
    }

    fun retrieveById(id: Long) = orderRepository.getReferenceById(id)

    fun retrieveAll(): List<Order> = orderRepository.findAll()

    fun isStarted(order: Order): Boolean = ordersProgress[order.id] != null

    fun createOrder(user: User): Order {
        val order = orderRepository.save(Order(guest = user))
        ordersProgress[order.id!!] = OrderProgress()
        return order
    }

    fun findOpenedOrderForUser(user: User): Order? = orderRepository.findOrdersByGuest(user)
        .firstOrNull { !it.state.isEqual(OrderState.CLOSED) && !it.state.isEqual(OrderState.CANCELLED) }

    fun addDishInOrder(order: Order, dish: Dish) {
        order.dishes.add(dish)
        orderRepository.save(order)
        dishService.reduceCount(dish)
        if (order.state.isEqual(OrderState.IN_PROGRESS)) {
            ordersProgress[order.id]?.add(dish.timeToReady)
        } else if (!order.state.isEqual(OrderState.OPENED)) {
            throw Exception("Invalid Order's state!")
        }
    }

    fun removeDishFromOrder(order: Order, dish: Dish) {
        if (dish !in order.dishes) {
            throw Exception("Dish not in order!")
        }
        order.dishes.remove(dish)
        orderRepository.save(order)
        dishService.addCount(dish)
    }

    fun getProgressForOrder(order: Order): Int = ordersProgress[order.id]?.getProgress() ?: 0

    fun takeOrder(order: Order) {
        order.id ?: throw Exception("Invalid order!")
        if (order.dishes.isEmpty()) {
            throw Exception("Order is empty!")
        }

        order.state = OrderState.IN_PROGRESS
        orderRepository.save(order)

        if (ordersProgress[order.id] == null) {
            ordersProgress[order.id] = OrderProgress()
            ordersProgress[order.id]!!.add(order.timeToReady)
        }
        val orderId = order.id
        Thread {
            ordersProgress[orderId]!!.start()
            while (ordersProgress[orderId]!!.shouldWait()) {
                Thread.sleep(1000)
            }
            readyOrders.add(orderId)
            ordersProgress.remove(orderId)
        }.start()
    }

    fun cancelOrder(order: Order) {
        if (!order.state.isEqual(OrderState.IN_PROGRESS)) {
            throw Exception("Order's state must be IN_PROGRESS for cancellation!")
        }
        order.state = OrderState.CANCELLED
        orderRepository.save(order)
    }

    fun payForOrder(order: Order) {
        if (!order.state.isEqual(OrderState.DONE)) {
            throw Exception("Order's state must be DONE for paying!")
        }
        order.state = OrderState.CLOSED
        orderRepository.save(order)
    }
}