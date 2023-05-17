package com.example.testapplogin.domain

import com.example.testapplogin.common.CheckAuth
import com.example.testapplogin.common.CheckAuthResponse
import com.example.testapplogin.common.Resource

interface CheckAuthRepo {
    suspend fun checkAuth(checkAuth: CheckAuth): Resource<CheckAuthResponse>

}