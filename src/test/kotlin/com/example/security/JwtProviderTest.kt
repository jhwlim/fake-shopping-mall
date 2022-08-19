package com.example.security

import com.example.config.JwtProperties
import com.example.domain.user.UserRole
import com.example.exception.BaseException
import com.example.exception.ErrorType
import io.jsonwebtoken.security.Keys
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

internal class JwtProviderTest : BehaviorSpec({

    val jwtProperties = mockk<JwtProperties>()

    val jwtProvider = JwtProvider(
        jwtProperties = jwtProperties
    )

    val secretKey = "d5f1d38bf511e4b42134324735a561f1d5f1d38bf511e4b42134324735a561f1"
    val expirationTime = 3600L
    val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    Given("createToken") {
        val user = SecurityUser(
            id = 1L,
            name = "테스트",
            role = UserRole.USER
        )
        val createdDtm = LocalDateTime.of(2022, 4, 25, 0, 0, 0)

        every { jwtProperties.key } returns key
        every { jwtProperties.expirationTime } returns expirationTime

        When("토큰을 생성하면") {
            val actual = jwtProvider.createToken(user, createdDtm)

            Then("인코딩된 문자열을 반환해야 한다.") {
                val expected = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwibmFtZSI6Iu2FjOyKpO2KuCIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjUwODE2MDAwfQ.mjdRxcbjWM0jX0LTyh22-TmaYvxVyWYj973aDoDm1sFQP_KVHPqZEMYG8NripkZy7tYFciWyr_RvfBdZ8XxqqQ"
                actual shouldBe expected
            }

        }
    }


    Given("decodeToken - 유효한 토큰") {
        val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwibmFtZSI6Iu2FjOyKpO2KuCIsInJvbGUiOiJVU0VSIiwiZXhwIjo0MTMzOTUxOTk5fQ.zvQ4c7cS7GVohFckm4PUjCL_qrhR6_BCMjXqJur6AEKtU6axIxTtPDhAwZeipEQ2jjL1gDMGNnEHCMBDaCuLyA"

        every { jwtProperties.key } returns key

        When("토큰을 디코딩하면") {
            val actual = jwtProvider.decodeToken(token)

            Then("사용자 정보를 반환해야 한다.") {
                actual.id shouldBe 1L
                actual.name shouldBe "테스트"
                actual.role shouldBe UserRole.USER
            }

        }

    }

    Given("decodeToken - key가 다른 경우") {
        val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwibmFtZSI6Iu2FjOyKpO2KuCIsInJvbGUiOiJVU0VSIiwiZXhwIjo0MTMzOTUxOTk5fQ.SoCLG0IzwzsA9sHKgslZy0jCOCqAfNmernWFpPikWj_fB9cncJPk1bdj62RZH5FwHHx1UWYSnCIcv55FbqoULA"

        every { jwtProperties.key } returns key

        When("토큰을 디코딩하면") {

            Then("예외가 발생해야 한다.") {
                val exception = shouldThrow<BaseException> { jwtProvider.decodeToken(token) }

                exception.errorType shouldBe ErrorType.INVALID_AUTH_TOKEN
            }

        }
    }

    Given("decodeToken - 만료된 경우") {
        val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwibmFtZSI6Iu2FjOyKpO2KuCIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNjUwODE2MDAwfQ.mjdRxcbjWM0jX0LTyh22-TmaYvxVyWYj973aDoDm1sFQP_KVHPqZEMYG8NripkZy7tYFciWyr_RvfBdZ8XxqqQ"

        every { jwtProperties.key } returns key

        When("토큰을 디코딩하면") {

            Then("예외가 발생해야 한다.") {
                val exception = shouldThrow<BaseException> { jwtProvider.decodeToken(token) }

                exception.errorType shouldBe ErrorType.INVALID_AUTH_TOKEN
            }

        }
    }

})
