package com.example

import com.example.exception.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.MvcResult

fun <T> ObjectMapper.readMvcResult(mvcResult: MvcResult, type: Class<T>): T = this.readValue(mvcResult.response.contentAsString, type)

fun ObjectMapper.readMvcResultToErrorResponse(mvcResult: MvcResult): ErrorResponse = this.readValue(mvcResult.response.contentAsString, ErrorResponse::class.java)
