package com.luci.aeris.domain.model


data class UserCredentials(
    val uid: String,
    val email: String?,
    val idToken: String // Firebase ID Token (JWT)
)
