package com.boot.kotlin.sc.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepo : JpaRepository<Product, Int>

@Repository
interface UserRepo : JpaRepository<User, Int>

@Repository
interface CartRepo : JpaRepository<Cart, Int> {
    fun findByUser(user: User): Cart?
}

@Repository
interface CartItemRepo : JpaRepository<CartItem, Int>