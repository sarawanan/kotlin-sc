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
class User(
    val name: String,
    val password: String,
    @Id @GeneratedValue val id: Int? = null,
)

@Entity
class Cart(
    @OneToOne val user: User,
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
