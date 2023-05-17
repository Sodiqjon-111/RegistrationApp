package com.example.testapplogin.domain

import com.example.testapplogin.api.ApiInterface
import com.example.testapplogin.common.Constants
import com.example.testapplogin.common.RefreshToken
import com.example.testapplogin.common.Resource
import com.example.testapplogin.common.TokenResponse
import retrofit2.Response
import javax.inject.Inject

class RefreshTokenRepoImp @Inject constructor(
    private val serviceApi: ApiInterface,
) : RefreshTokenRepo {
    override suspend fun refreshToken(refreshToken: RefreshToken): Resource<Response<TokenResponse>> {
        return try {
            val result = serviceApi.refreshToken(refreshToken)
            if (result.code() == Constants.SuccessCode) {
                Resource.Success(result)
            } else {
                Resource.Error(data = null,result.message())
            }
        } catch (e: Exception) {
            Resource.Error(null,e.message)
        }
    }
}
