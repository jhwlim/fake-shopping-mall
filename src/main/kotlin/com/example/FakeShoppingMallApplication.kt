package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class FakeShoppingMallApplication

fun main(args: Array<String>) {
    runApplication<FakeShoppingMallApplication>(*args)
}
