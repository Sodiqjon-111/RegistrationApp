package com.example.testapplogin.domain

import com.example.testapplogin.api.ApiInterface
import com.example.testapplogin.common.PhoneResponse
import com.example.testapplogin.common.Resource
import com.example.testapplogin.common.SendAuth
import javax.inject.Inject

class PhoneNumberRepoImp @Inject constructor(
    private val serviceApi: ApiInterface,
) : PhoneNumberRepo {
    override suspend fun sendPhoneNumber(sendAuth: SendAuth): Resource<PhoneResponse> {
        return try {
            val result =
                serviceApi.sendAuthCode(sendAuth)
            if (result.body()?.is_success == true) {
                Resource.Success(result.body())
            } else {
                Resource.Error(data = null, result.message())
            }
        } catch (e: Exception) {
            Resource.Error(null, e.message)
        }
    }
}