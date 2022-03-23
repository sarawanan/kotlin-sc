package com.boot.kotlin.sc.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepo : JpaRepository<Product, Int>

@Repository
interface CustomerRepo : JpaRepository<Customer, Int>

@Repository
interface CartRepo : JpaRepository<Cart, Int> {
    fun findByCustomer(customer: Customer): Cart?
}

@Repository
interface CartItemRepo : JpaRepository<CartItem, Int>