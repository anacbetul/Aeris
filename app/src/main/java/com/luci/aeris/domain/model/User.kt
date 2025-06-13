package com.luci.aeris.domain.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val deviceToken: String? = null,
    val password: String = "",
    val gender: String= ""
)
