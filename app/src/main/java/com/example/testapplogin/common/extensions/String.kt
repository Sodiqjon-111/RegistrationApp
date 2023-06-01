package com.example.testapplogin.common.extensions

fun String?.isUsername(): Boolean {
    if (this.isNullOrBlank()) return false
    val usernameRegex = "^[a-zA-Z0-9_-]{5,20}$"
    return usernameRegex.toRegex().matches(this)
}