package com.example.testapplogin.domain

import com.example.testapplogin.api.ApiInterface
import com.example.testapplogin.common.Registration
import com.example.testapplogin.common.RegistrationResponse
import com.example.testapplogin.common.Resource
import javax.inject.Inject

class RegistrationRepoImp @Inject constructor(
    private val serviceApi: ApiInterface,
) : RegistrationRepo {
    override suspend fun registration(registration: Registration): Resource<RegistrationResponse> {
        return try {
            val result =
                serviceApi.registration(registration)
            if (result.code() == 201) {
                Resource.Success(result.body())
            } else {
                Resource.Error(data = null, result.message())
            }
        } catch (e: Exception) {
            Resource.Error(null, e.message)
        }
    }
}