package com.example.testapplogin.domain

import com.example.testapplogin.api.ApiInterface
import com.example.testapplogin.common.Constants
import com.example.testapplogin.common.Profile
import com.example.testapplogin.common.Resource
import com.orhanobut.hawk.Hawk
import retrofit2.Response
import javax.inject.Inject

class ProfileRepoImp @Inject
constructor(
    private val serviceApi: ApiInterface,
) : ProfileRepo {
    override suspend fun getProfile(): Resource<Response<Profile>>{
        return try {
            val result =
                serviceApi.getProfile("Bearer " + Hawk.get("accessToken"))
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