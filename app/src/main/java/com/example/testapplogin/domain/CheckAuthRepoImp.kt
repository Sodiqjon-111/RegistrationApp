package com.example.testapplogin.domain

import com.example.testapplogin.api.ApiInterface
import com.example.testapplogin.common.CheckAuth
import com.example.testapplogin.common.CheckAuthResponse
import com.example.testapplogin.common.Constants
import com.example.testapplogin.common.Resource
import javax.inject.Inject

class CheckAuthRepoImp @Inject constructor(
    private val serviceApi: ApiInterface,
) : CheckAuthRepo {
    override suspend fun checkAuth(checkAuth: CheckAuth): Resource<CheckAuthResponse> {
        return try {
            val result =
                serviceApi.checkAuthCode(checkAuth)
            if (result.code() == Constants.SuccessCode) {
                Resource.Success(result.body())
            } else {
                Resource.Error(data = null, result.message())
            }
        } catch (e: Exception) {
            Resource.Error(null, e.message)
        }
    }
}