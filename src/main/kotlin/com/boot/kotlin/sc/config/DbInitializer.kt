package com.boot.kotlin.sc.config

import com.boot.kotlin.sc.entity.Customer
import com.boot.kotlin.sc.entity.CustomerRepo
import com.boot.kotlin.sc.entity.Product
import com.boot.kotlin.sc.entity.ProductRepo
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DbInitializer {
    @Bean
    fun initialize(customerRepo: CustomerRepo, productRepo: ProductRepo) = ApplicationRunner {
        customerRepo.saveAndFlush(Customer("Sara", "Test address"))
        productRepo.saveAndFlush(Product("Rice", 10, 100.00))
        productRepo.saveAndFlush(Product("Wheat", 20, 200.00))
    }
}