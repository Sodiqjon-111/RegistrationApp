package com.example.testapplogin.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class PhoneResponse(
    val is_success: Boolean
)

data class SendAuth(
    val phone: String,
)

data class CheckAuth(
    val phone: String,
    val code: String,
)

data class Registration(
    val phone: String,
    val name: String,
    val username: String,

    )

@Parcelize
data class RefreshToken(
    val refresh_token: String,
) : Parcelable

data class TokenResponse(
    val refresh_token: String,
    val access_token: String,
)

data class CheckAuthResponse(
    val refresh_token: String,
    val access_token: String,
    val user_id: Int,
    val is_user_exists: Boolean,
)

@Parcelize
data class Profile(
    val profile_data: ProfileResponse?
) : Parcelable

@Parcelize
data class ProfileResponse(
    val name: String?,
    val username: String?,
    val phone: String?,
    val city: String?,
    val instagram: String?,
    val status: String?,
    val avatar: String?,
) : Parcelable


data class RegistrationResponse(
    val refresh_token: String,
    val access_token: String,
    val user_id: Int,
)

@Parcelize
data class UpdateProfile(
    val name: String?,
    val username: String?,
    val city: String?,
    val instagram: String?,
    val status: String?,
    val avatar: Avatar?,
) : Parcelable


@Parcelize
data class Avatar(
    var filename: String?,
    var base_64: String?
) : Parcelable

