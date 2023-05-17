package com.example.testapplogin.domain

import com.example.testapplogin.common.Profile
import com.example.testapplogin.common.Resource
import retrofit2.Response

interface ProfileRepo {
    suspend fun getProfile(): Resource<Response<Profile>>

}