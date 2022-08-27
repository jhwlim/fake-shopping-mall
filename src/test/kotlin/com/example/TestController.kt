package com.example

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/auth")
    fun test(): String {
        return "OK"
    }

    @GetMapping("/permitAll")
    fun testPermitAll(): String {
        return "OK"
    }

}
