package com.boot.kotlin.sc

import com.boot.kotlin.sc.dto.RequestDto
import com.boot.kotlin.sc.entity.*
import com.boot.kotlin.sc.exception.CartNotFoundException
import com.boot.kotlin.sc.exception.CustomerNotFoundException
import com.boot.kotlin.sc.exception.ProductNotFoundException
import com.fasterxml.jackson.module.kotlin.jsonMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest
class ControllerTests @Autowired constructor(val mockMvc: MockMvc) {
    @MockBean
    private lateinit var customerRepo: CustomerRepo

    @MockBean
    private lateinit var productRepo: ProductRepo

    @MockBean
    private lateinit var cartItemRepo: CartItemRepo

    @MockBean
    private lateinit var cartRepo: CartRepo

    @Test
    fun `test create and get product`() {
        val product = Product("Rice", 10, 100.00)
        Mockito.`when`(productRepo.save(Mockito.any()))
            .thenReturn(product)
        mockMvc.perform(post("/api/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper().writeValueAsString(product)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Rice"))
    }

    @Test
    fun `test get all products`() {
        Mockito.`when`(productRepo.findAll()).thenReturn(
            listOf(Product("Rice", 10, 100.00), Product("Wheat", 20, 200.00)))
        mockMvc.perform(get("/api/product"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.[0].name").value("Rice"))
            .andExpect(jsonPath("$.[1].price").value(200.00))
    }

    @Test
    fun `test create and get customer`() {
        val customer = Customer("Sara", "Test address")
        Mockito.`when`(customerRepo.save(Mockito.any()))
            .thenReturn(customer)
        mockMvc.perform(post("/api/customer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper().writeValueAsString(customer)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Sara"))
    }

    @Test
    fun `test get all customers`() {
        Mockito.`when`(customerRepo.findAll()).thenReturn(
            listOf(Customer("Sara", "Test address"), Customer("Maha", "Address")))
        mockMvc.perform(get("/api/customer"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.[0].name").value("Sara"))
            .andExpect(jsonPath("$.[1].address").value("Address"))
    }

    @Test
    fun `test create cart with customer not found exception`() {
        Mockito.`when`(customerRepo.findByIdOrNull(Mockito.anyInt()))
            .thenThrow(CustomerNotFoundException("Customer Not Found!"))
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/1"))
            .andExpect(status().`is`(404))
    }

    @Test
    fun `test create cart findByCustomer`() {
        val customer = Optional.of(Customer("Sara", "address"))
        Mockito.`when`(customerRepo.findById(Mockito.anyInt()))
            .thenReturn(customer)
        val cart = Cart(customer.get(), 200.00,
            mutableListOf(CartItem(1, 10, 200.00)))
        Mockito.`when`(cartRepo.findByCustomer(customer.get()))
            .thenReturn(cart)
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun `test create cart save`() {
        val customer = Optional.of(Customer("Sara", "address"))
        Mockito.`when`(customerRepo.findById(Mockito.anyInt()))
            .thenReturn(customer)
        val cart = Cart(customer.get(), 200.00,
            mutableListOf(CartItem(1, 10, 200.00)))
        Mockito.`when`(cartRepo.save(Mockito.any())).thenReturn(cart)
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun `test get cart by id - exception`() {
        Mockito.`when`(cartRepo.findById(Mockito.anyInt())).thenThrow(CartNotFoundException("Cart Not Found!"))
        mockMvc.perform(get("/api/cart/1"))
            .andExpect(status().`is`(404))
    }

    @Test
    fun `test get cart by id`() {
        val customer = Optional.of(Customer("Sara", "address"))
        val cart = Optional.of(Cart(customer.get(), 200.00,
            mutableListOf(CartItem(1, 10, 200.00))))
        Mockito.`when`(cartRepo.findById(Mockito.anyInt()))
            .thenReturn(cart)
        mockMvc.perform(get("/api/cart/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun `test add to cart - cart not found exception`() {
        val json = jsonMapper().writeValueAsString(RequestDto(1, 10))
        Mockito.`when`(cartRepo.findById(Mockito.anyInt())).thenThrow(CartNotFoundException("Cart Not Found!"))
        mockMvc.perform(post("/api/cart/1/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().`is`(404))
    }

    @Test
    fun `test add to cart - product not found exception`() {
        val json = jsonMapper().writeValueAsString(RequestDto(1, 10))
        val customer = Optional.of(Customer("Sara", "address"))
        val cart = Optional.of(Cart(customer.get(), 200.00,
            mutableListOf(CartItem(1, 10, 200.00))))
        Mockito.`when`(cartRepo.findById(Mockito.anyInt()))
            .thenReturn(cart)
        Mockito.`when`(productRepo.findById(Mockito.anyInt())).thenThrow(ProductNotFoundException("Product Not Found!"))
        mockMvc.perform(post("/api/cart/1/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().`is`(404))
    }

    @Test
    fun `test add to cart - qty not available exception`() {
        val json = jsonMapper().writeValueAsString(RequestDto(1, 100))
        val customer = Optional.of(Customer("Sara", "address", 1))
        val product = Optional.of(Product("Rice", 10, 100.00, 1))
        val cart = Optional.of(Cart(customer.get(), 200.00,
            mutableListOf(CartItem(1, 10, 200.00))))
        Mockito.`when`(cartRepo.findById(Mockito.anyInt()))
            .thenReturn(cart)
        Mockito.`when`(productRepo.findById(Mockito.anyInt()))
            .thenReturn(product)
        mockMvc.perform(post("/api/cart/1/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().`is`(400))
    }

    @Test
    fun `test add to cart`() {
        val json = jsonMapper().writeValueAsString(RequestDto(1, 5))
        val customer = Optional.of(Customer("Sara", "address", 1))
        val product = Optional.of(Product("Rice", 10, 100.00, 1))
        val cart = Optional.of(Cart(customer.get(), 200.00,
            mutableListOf(CartItem(1, 10, 200.00))))
        val cartItem = CartItem(1, 5, 500.00, 1)
        Mockito.`when`(cartRepo.findById(Mockito.anyInt())).thenReturn(cart)
        Mockito.`when`(productRepo.findById(Mockito.anyInt())).thenReturn(product)
        Mockito.`when`(cartItemRepo.save(Mockito.any())).thenReturn(cartItem)
        Mockito.`when`(cartRepo.save(Mockito.any())).thenReturn(cart.get())
        mockMvc.perform(post("/api/cart/1/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.customer.name").value("Sara"))
            .andExpect(jsonPath("$.cartItems.[0].price").value(200.00))
            .andExpect(jsonPath("$.cartItems.[1].price").value(500.00))
            .andExpect(jsonPath("$.totalPrice").value(700))
    }
}