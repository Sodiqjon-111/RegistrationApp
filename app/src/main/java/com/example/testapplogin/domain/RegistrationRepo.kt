package com.example.testapplogin.domain

import com.example.testapplogin.common.Registration
import com.example.testapplogin.common.RegistrationResponse
import com.example.testapplogin.common.Resource

interface RegistrationRepo {
    suspend fun registration(registration: Registration): Resource<RegistrationResponse>

}