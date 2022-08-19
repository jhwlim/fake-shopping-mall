package com.example.exception

data class ErrorResponse(
    val code: Int,
    val message: String,
) {

    companion object {

        fun from(errorType: ErrorType): ErrorResponse {
            return errorType.run {
                ErrorResponse(
                    code = code,
                    message = message,
                )
            }
        }

    }

}
