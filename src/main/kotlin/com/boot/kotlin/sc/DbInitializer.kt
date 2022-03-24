package com.boot.kotlin.sc

import com.boot.kotlin.sc.entity.*
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DbInitializer {
    @Bean
    fun initialize(
        customerRepo: CustomerRepo, productRepo: ProductRepo,
        cartRepo: CartRepo, cartItemRepo: CartItemRepo,
    ) = ApplicationRunner {
        customerRepo.saveAndFlush(Customer("Sara", "Test address"))
        productRepo.saveAndFlush(Product("Rice", 10, 100.00))
        productRepo.saveAndFlush(Product("Wheat", 20, 200.00))
    }
}