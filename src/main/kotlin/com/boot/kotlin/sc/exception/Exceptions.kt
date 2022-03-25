package com.boot.kotlin.sc.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(s: String?) : IllegalArgumentException(s)

@ResponseStatus(HttpStatus.NOT_FOUND)
class CartNotFoundException(s: String?) : IllegalArgumentException(s)

@ResponseStatus(HttpStatus.NOT_FOUND)
class ProductNotFoundException(s: String?) : IllegalArgumentException(s)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class QuantityNotAvailableException(s: String?) : IllegalArgumentException(s)