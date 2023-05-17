package com.example.testapplogin.responses

data class BaseResponse<T>(
    val data: T,
    val success: Boolean,
)