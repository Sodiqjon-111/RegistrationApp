package com.example.testapplogin.api

import com.example.testapplogin.common.CheckAuth
import com.example.testapplogin.common.CheckAuthResponse
import com.example.testapplogin.common.PhoneResponse
import com.example.testapplogin.common.Profile
import com.example.testapplogin.common.ProfileResponse
import com.example.testapplogin.common.RefreshToken
import com.example.testapplogin.common.Registration
import com.example.testapplogin.common.RegistrationResponse
import com.example.testapplogin.common.SendAuth
import com.example.testapplogin.common.TokenResponse
import com.example.testapplogin.common.UpdateProfile
import com.example.testapplogin.responses.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiInterface {

    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(
        @Body sendAuth: SendAuth,
        // @Header("Authorization") auth: String
    ): Response<PhoneResponse>

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(
        @Body checkAuth: CheckAuth,
    ): Response<CheckAuthResponse>

    @GET("/api/v1/users/me/")
    suspend fun getProfile(
        @Header("Authorization") auth: String
    ): Response<Profile>

    @POST("/api/v1/users/register/")
    suspend fun registration(
        @Body registration: Registration,
      //  @Header("Authorization") auth: String
    ): Response<RegistrationResponse>

    @PUT("/api/v1/users/me/")
    suspend fun updateProfile(
        @Body updateProfile: UpdateProfile,
        @Header("Authorization") auth: String
    ): Response<Any>

    @POST("/api/v1/users/refresh-token/")
    suspend fun refreshToken(
        @Body refreshToken: RefreshToken,
    ): Response<TokenResponse>


}