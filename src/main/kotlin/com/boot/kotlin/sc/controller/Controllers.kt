package com.boot.kotlin.sc.controller

import com.boot.kotlin.sc.dto.RequestDto
import com.boot.kotlin.sc.entity.*
import com.boot.kotlin.sc.exception.CartNotFoundException
import com.boot.kotlin.sc.exception.UserNotFoundException
import com.boot.kotlin.sc.exception.ProductNotFoundException
import com.boot.kotlin.sc.exception.QuantityNotAvailableException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ProductController(@Autowired val productRepo: ProductRepo) {
    @PostMapping("/product")
    fun createProduct(@RequestBody product: Product) = productRepo.save(product)

    @GetMapping("/product")
    fun getProducts(): List<Product> = productRepo.findAll()
}

@RestController
@RequestMapping("/api")
class UserController(@Autowired val userRepo: UserRepo) {
    @PostMapping("/user")
    fun createUser(@RequestBody customer: User) = userRepo.save(customer)

    @GetMapping("/user")
    fun getUser(): List<User> = userRepo.findAll()
}

@RestController
@RequestMapping("/api")
class CartController @Autowired constructor(
    val userRepo: UserRepo,
    val productRepo: ProductRepo,
    val cartItemRepo: CartItemRepo,
    val cartRepo: CartRepo,
) {
    @PostMapping("/cart/{userId}")
    fun createCart(@PathVariable userId: Int): Cart {
        val user = userRepo.findByIdOrNull(userId) ?: throw UserNotFoundException("User Not Found!")
        return cartRepo.findByUser(user) ?: cartRepo.save(Cart(user, 0.00))
    }

    @PostMapping("/cart/{cartId}/add")
    fun addToCart(@PathVariable cartId: Int, @RequestBody requestDto: RequestDto): Cart {
        val cart = cartRepo.findByIdOrNull(cartId) ?: throw CartNotFoundException("Cart Not Found!")
        val product =
            productRepo.findByIdOrNull(requestDto.productId) ?: throw ProductNotFoundException("Product Not Found!")
        if (product.qty < requestDto.qty) throw QuantityNotAvailableException("Requested quantity not available")
        val price = product.price * requestDto.qty
        cart.cartItems?.add(cartItemRepo.save(CartItem(product.id!!, requestDto.qty, price)))
        cart.totalPrice += price
        product.qty -= requestDto.qty
        productRepo.save(product)
        return cartRepo.save(cart)
    }

    @GetMapping("/cart/{cartId}")
    fun getCart(@PathVariable cartId: Int): Cart {
        return cartRepo.findByIdOrNull(cartId) ?: throw CartNotFoundException("Cart Not Found!")
    }
}