package com.boot.kotlin.sc.config

import com.boot.kotlin.sc.entity.Product
import com.boot.kotlin.sc.entity.ProductRepo
import com.boot.kotlin.sc.entity.User
import com.boot.kotlin.sc.entity.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration

@Configuration
class DbInitializer @Autowired constructor(val userRepo: UserRepo, val productRepo: ProductRepo) :
    ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        userRepo.saveAndFlush(User("admin", "admin"))
        productRepo.saveAndFlush(Product("Rice", 10, 100.00))
        productRepo.saveAndFlush(Product("Wheat", 20, 200.00))
    }
}