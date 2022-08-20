package com.example.controller

import com.example.config.SecurityConfig
import com.example.controller.dto.LoginRequest
import com.example.controller.dto.LoginResponse
import com.example.exception.ErrorType
import com.example.readMvcResult
import com.example.readMvcResultToErrorResponse
import com.example.service.AuthService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Import(SecurityConfig::class)
@WebMvcTest(AuthController::class)
@AutoConfigureMockMvc
internal class AuthControllerTest(
    @Autowired
    val mockMvc: MockMvc,
    @MockkBean
    val authService: AuthService,
    @Autowired
    val objectMapper: ObjectMapper,
) : BehaviorSpec({

    Given("로그인 API") {
        val request = LoginRequest(
            name = "test",
            password = "1234"
        )

        every { authService.createAccessToken(any(), any()) } returns "accessToken"

        When("로그인에 성공하면") {
            val mvcResult = mockMvc.perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andReturn()

            Then("로그인 성공 응답을 반환한다.") {
                val actual = objectMapper.readMvcResult(mvcResult, LoginResponse::class.java)

                actual.accessToken shouldBe "accessToken"
            }

        }

    }

    Given("로그인 API - 유효하지 않은 요청") {
        listOf(null, "", " ", "name").forAll { name ->
            listOf(null, "", " ").forAll { password ->
                val request = mapOf(
                    "name" to name,
                    "password" to password
                )

                When("로그인에 실패하면") {
                    val mvcResult = mockMvc.perform(
                        post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isBadRequest)
                        .andReturn()

                    Then("에러 응답을 반환한다.") {
                        val actual = objectMapper.readMvcResultToErrorResponse(mvcResult)

                        actual.code shouldBe ErrorType.INVALID_REQUEST.code
                    }

                }
            }
        }
    }

})
