package com.example.testapplogin.domain

import com.example.testapplogin.common.RefreshToken
import com.example.testapplogin.common.Resource
import com.example.testapplogin.common.TokenResponse
import retrofit2.Response

interface RefreshTokenRepo {
    suspend fun refreshToken(refreshToken: RefreshToken): Resource<Response<TokenResponse>>
}