package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FakeShoppingMallApplication

fun main(args: Array<String>) {
    runApplication<FakeShoppingMallApplication>(*args)
}
