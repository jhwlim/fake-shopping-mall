package com.example.security

import com.example.common.Constants
import com.example.exception.BaseException
import com.example.exception.ErrorType
import com.example.utils.writeErrorResponseBody
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val log = KotlinLogging.logger {}

class JwtAuthenticationFilter(
    authManager: AuthenticationManager,
    private val jwtProvider: JwtProvider,
) : BasicAuthenticationFilter(authManager) {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        try {
            val authorization = request.getHeader(Constants.AUTH_HEADER_NAME)
            authorization?.let {
                validateAuthorizationHeader(authorization)
                val token = getToken(authorization)
                val securityUser = jwtProvider.decodeToken(token)
                SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(securityUser, null, securityUser.authorities)
            }
            chain.doFilter(request, response)
        } catch (e: BaseException) {
            log.error { e.message }
            response.writeErrorResponseBody(e.errorType)
        } catch (e: Exception) {
            e.printStackTrace()
            response.writeErrorResponseBody(ErrorType.UNKNOWN)
        }
    }

    private fun validateAuthorizationHeader(authorization: String) {
        if (!authorization.startsWith(Constants.AUTH_ACCESS_TOKEN_TYPE)) {
            throw BaseException(ErrorType.INVALID_AUTHORIZATION_HEADER_FORMAT)
        }
    }

    private fun getToken(authorization: String): String {
        return authorization.substring(Constants.AUTH_ACCESS_TOKEN_TYPE.length + 1)
    }

}
