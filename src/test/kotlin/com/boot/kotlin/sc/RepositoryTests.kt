package com.boot.kotlin.sc

import com.boot.kotlin.sc.entity.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class RepositoryTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val productRepo: ProductRepo,
    val customerRepo: CustomerRepo,
    val cartRepo: CartRepo,
    val cartItemRepo: CartItemRepo,
) {
    @Test
    fun `test create and get the product`() {
        val savedProduct = entityManager.persist(Product("Rice", 10, 100.00))
        val foundProduct = productRepo.findByIdOrNull(savedProduct.id)
        assertEquals(savedProduct, foundProduct)
    }

    @Test
    fun `test create and get the customer`() {
        val savedCustomer = entityManager.persist(Customer("Sara", "Test address"))
        val foundCustomer = customerRepo.findByIdOrNull(savedCustomer.id)
        assertEquals(savedCustomer, foundCustomer)
    }

    @Test
    fun `test create and get cart`() {
        val savedCustomer = entityManager.persist(Customer("Sara", "Test address"))
        val savedProduct = entityManager.persist(Product("Rice", 10, 100.00))
        val savedItem1 = entityManager.persist(CartItem(savedProduct.id!!, 2, savedProduct.price * 2))
        val savedCart = entityManager.persist(Cart(savedCustomer, 200.00, mutableListOf(savedItem1)))
        val foundCart = cartRepo.findByIdOrNull(savedCart.id)
        assertEquals(savedCart, foundCart)
        assertTrue(foundCart?.cartItems?.size == 1)
    }

    @Test
    fun `test create and get cart item`() {
        val savedProduct = entityManager.persist(Product("Rice", 10, 100.00))
        val savedItem = entityManager.persist(CartItem(savedProduct.id!!, 2, savedProduct.price * 2))
        val foundItem = cartItemRepo.findByIdOrNull(savedItem.id)
        assertEquals(savedItem, foundItem)
    }
}
