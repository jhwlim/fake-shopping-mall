package com.example.security

import com.example.TestController
import com.example.TestSecurityConfig
import com.example.common.Constants
import com.example.common.enums.UserRole
import com.example.exception.BaseException
import com.example.exception.ErrorType
import com.example.readMvcResultToErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TestController::class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = [TestSecurityConfig::class])
internal class JwtAuthenticationFilterTest(
    @Autowired
    val mockMvc: MockMvc,
    @MockkBean
    val jwtProvider: JwtProvider,
    @Autowired
    val objectMapper: ObjectMapper,
): BehaviorSpec({

    val securityUser = SecurityUser(
        id = 1L,
        name = "test",
        role = UserRole.USER,
    )

    Given("유효한 Authorization Header") {
        val authorizationHeader = "${Constants.AUTH_ACCESS_TOKEN_TYPE} accessToken"
        every { jwtProvider.decodeToken(any()) } returns securityUser

        When("인증이 필요한 API 요청을 할 때,") {
            val mvcResult = mockMvc.perform(get("/test/auth")
                .header(Constants.AUTH_HEADER_NAME, authorizationHeader))
                .andExpect(status().isOk)
                .andReturn()

            Then("'OK' 문자열을 반환해야 한다.") {
                val actual = mvcResult.response.contentAsString
                actual shouldBe "OK"
            }
        }

        When("인증이 필요하지 않은 API 요청을 할 때,") {
            val mvcResult = mockMvc.perform(get("/test/permitAll")
                .header(Constants.AUTH_HEADER_NAME, authorizationHeader))
                .andExpect(status().isOk)
                .andReturn()

            Then("'OK' 문자열을 반환해야 한다.") {
                val actual = mvcResult.response.contentAsString
                actual shouldBe "OK"
            }
        }

    }

    Given("토큰이 없는 경우") {

        When("인증이 필요한 API 요청을 할 때,") {
            val mvcResult = mockMvc.perform(get("/test/auth"))
                .andExpect(status().isForbidden)
                .andReturn()

            Then("에러 응답을 반환해야 한다.") {
                val actual = objectMapper.readMvcResultToErrorResponse(mvcResult)
                actual.code shouldBe ErrorType.ACCESS_DENIED.code
            }
        }

        When("인증이 필요하지 않은 API 요청을 할 때,") {
            val mvcResult = mockMvc.perform(get("/test/permitAll"))
                .andExpect(status().isOk)
                .andReturn()

            Then("'OK' 문자열을 반환해야 한다.") {
                val actual = mvcResult.response.contentAsString
                actual shouldBe "OK"
            }
        }

    }

    Given("Authorization Header 형식이 잘못된 경우") {
        val authorizationHeader = "BBearer accessToken"

        When("인증이 필요한 API 요청을 할 때,") {
            val mvcResult = mockMvc.perform(get("/test/auth")
                .header(Constants.AUTH_HEADER_NAME, authorizationHeader))
                .andExpect(status().isUnauthorized)
                .andReturn()

            Then("에러 응답을 반환해야 한다.") {
                val actual = objectMapper.readMvcResultToErrorResponse(mvcResult)
                actual.code shouldBe ErrorType.INVALID_AUTHORIZATION_HEADER_FORMAT.code
            }
        }

    }

    Given("토큰이 만료되었거나 토큰을 디코딩하는 과정에서 예외가 발생하는 경우") {
        val authorizationHeader = "Bearer accessToken"
        every { jwtProvider.decodeToken(any()) } throws BaseException(ErrorType.INVALID_AUTH_TOKEN)

        When("인증이 필요한 API 요청을 할 때,") {
            val mvcResult = mockMvc.perform(get("/test/auth")
                .header(Constants.AUTH_HEADER_NAME, authorizationHeader))
                .andExpect(status().isUnauthorized)
                .andReturn()

            Then("에러 응답을 반환해야 한다.") {
                val actual = objectMapper.readMvcResultToErrorResponse(mvcResult)
                actual.code shouldBe ErrorType.INVALID_AUTH_TOKEN.code
            }
        }

    }

})
