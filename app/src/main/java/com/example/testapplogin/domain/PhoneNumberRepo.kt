package com.example.testapplogin.domain

import com.example.testapplogin.common.PhoneResponse
import com.example.testapplogin.common.Resource
import com.example.testapplogin.common.SendAuth

interface PhoneNumberRepo {
    suspend fun sendPhoneNumber(sendAuth: SendAuth): Resource<PhoneResponse>

}