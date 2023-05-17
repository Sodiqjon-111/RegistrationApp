package com.example.testapplogin.domain

import com.example.testapplogin.common.Resource
import com.example.testapplogin.common.UpdateProfile
import retrofit2.Response

interface UpdateProfileRepo {
    suspend fun updateProfile(updateProfile: UpdateProfile): Resource<Response<Any>>

}