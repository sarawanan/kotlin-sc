package com.boot.kotlin.sc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebKotlinApplication

fun main(args: Array<String>) {
    runApplication<WebKotlinApplication>(*args)
}
