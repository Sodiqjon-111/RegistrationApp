package com.example.testapplogin.domain

import com.example.testapplogin.api.ApiInterface
import com.example.testapplogin.common.Constants
import com.example.testapplogin.common.Resource
import com.example.testapplogin.common.UpdateProfile
import com.orhanobut.hawk.Hawk
import retrofit2.Response
import javax.inject.Inject

class UpdateProfileRepoImp @Inject
constructor(
    private val serviceApi: ApiInterface,
) : UpdateProfileRepo {
    override suspend fun updateProfile(updateProfile: UpdateProfile): Resource<Response<Any>> {
        return try {
            val result =
                serviceApi.updateProfile(updateProfile, "Bearer " + Hawk.get("accessToken"))
            if (result.code() == Constants.SuccessCode) {
                Resource.Success(result)
            } else {
                if (result.code() == Constants.ExpiredTokenCode) {
                    Hawk.put("ExpiredAccessToken", 401)
                } else {
                }
                Resource.Error(data = null, result.message())
            }
        } catch (e: Exception) {
            Resource.Error(null, e.message)
        }
    }
}