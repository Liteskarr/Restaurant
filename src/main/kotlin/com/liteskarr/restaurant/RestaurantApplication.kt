package com.liteskarr.restaurant

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.orm.hibernate5.LocalSessionFactoryBean


@SpringBootApplication
class RestaurantApplication


@Bean
fun sessionFactory(): LocalSessionFactoryBean {
    return LocalSessionFactoryBean()
}

fun main(args: Array<String>) {
    runApplication<RestaurantApplication>(*args)
}
