package com.boot.kotlin.sc.entity

import javax.persistence.*

@Entity
class Product(
    val name: String,
    var qty: Int,
    val price: Double,
    @Id @GeneratedValue val id: Int? = null,
)

@Entity
class Customer(
    val name: String,
    val address: String,
    @Id @GeneratedValue val id: Int? = null,
)

@Entity
class Cart(
    @OneToOne val customer: Customer,
    var totalPrice: Double,
    @OneToMany(cascade = [CascadeType.ALL])
    var cartItems: MutableList<CartItem>? = null,
    @Id @GeneratedValue val id: Int? = null,
)

@Entity
class CartItem(
    val productId: Int,
    val qty: Int,
    val price: Double,
    @Id @GeneratedValue var id: Int? = null,
)
