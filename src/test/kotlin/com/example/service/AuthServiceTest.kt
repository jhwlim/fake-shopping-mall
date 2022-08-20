package com.example.service

import com.example.common.enums.UserRole
import com.example.domain.user.User
import com.example.domain.user.UserRepository
import com.example.exception.BaseException
import com.example.exception.ErrorType
import com.example.model.UserDto
import com.example.security.JwtProvider
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
    val user = UserDto(
        id = 1L,
        name = name,
        role = UserRole.USER
    )

    Given("createAccessToken") {

        every { jwtProvider.createToken(any(), any()) } returns "accessToken"

        When("액세스 토큰을 생성하면") {
            val actual = authService.createAccessToken(user)

            Then("문자열 토큰이 생성된다.") {
                actual shouldBe "accessToken"
            }

        }

    }

    Given("login") {

        every { userRepository.findByName(name) } returns foundUser

        When("사용자를 인증하면") {
            val actual = authService.login(name, password)

            Then("사용자 정보를 반환해야 한다.") {
                actual shouldBe user
            }

        }

    }

    Given("login - 사용자를 찾을 수 없는 경우") {

        every { userRepository.findByName(name) } returns null

        When("사용자를 인증하면") {

            Then("예외가 발생해야 한다.") {
                val exception = shouldThrow<BaseException> { authService.login(name, password) }

                exception.errorType shouldBe ErrorType.USER_NAME_NOT_FOUND
            }

        }

    }

    Given("login - 패스워드가 일치하지 않는 경우") {
        val wrongPassword = "1235"

        every { userRepository.findByName(name) } returns foundUser

        When("사용자를 인증하면") {

            Then("예외가 발생해야 한다.") {
                val exception = shouldThrow<BaseException> { authService.login(name, wrongPassword) }

                exception.errorType shouldBe ErrorType.PASSWORD_NOT_MATCHES
            }

        }

    }

})
