package com.example.service

import com.example.domain.user.User
import com.example.domain.user.UserRepository
import com.example.domain.user.UserRole
import com.example.exception.BaseException
import com.example.exception.ErrorType
import com.example.security.JwtProvider
import com.example.security.SecurityUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

internal class AuthServiceTest : BehaviorSpec({

    val userRepository = mockk<UserRepository>()
    val jwtProvider = mockk<JwtProvider>()
    val authService = AuthService(
        userRepository = userRepository,
        jwtProvider = jwtProvider,
    )

    val name = "test"
    val password = "1234"
    val foundUser = User(
        id = 1L,
        name = name,
        password = password,
        role = UserRole.USER
    )

    Given("createAccessToken - 일치하는 이름의 사용자가 존재하고, 패스워드가 일치하는 경우") {

        every { userRepository.findByName(name) } returns foundUser
        every { jwtProvider.createToken(SecurityUser.from(foundUser), any()) } returns "accessToken"

        When("액세스 토큰을 생성하면") {
            val actual = authService.createAccessToken(name, password)

            Then("문자열 토큰이 생성된다.") {
                val expected = "accessToken"

                actual shouldBe expected
            }

        }

    }

    Given("createAccessToken - 사용자를 찾을 수 없는 경우") {

        every { userRepository.findByName(name) } returns null

        When("액세스 토큰을 생성하면") {

            Then("예외가 발생해야 한다.") {
                val exception = shouldThrow<BaseException> { authService.createAccessToken(name, password) }

                exception.errorType shouldBe ErrorType.USER_NOT_FOUND
            }

        }

    }

    Given("createAccessToken - 패스워드가 일치하지 않는 경우") {
        val wrongPassword = "1235"

        every { userRepository.findByName(name) } returns foundUser

        When("액세스 토큰을 생성하면") {

            Then("예외가 발생해야 한다.") {
                val exception = shouldThrow<BaseException> { authService.createAccessToken(name, wrongPassword) }

                exception.errorType shouldBe ErrorType.PASSWORD_NOT_MATCHES
            }

        }

    }

})
