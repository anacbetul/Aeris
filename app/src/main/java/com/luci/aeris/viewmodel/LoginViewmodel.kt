package com.luci.aeris.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var emailError = mutableStateOf(false)
    var passwordError = mutableStateOf(false)

    // Email ve password doğrulama fonksiyonu
    fun isFormValid(): Boolean {
        // Email validasyonu
        emailError.value = email.value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()

        // Password validasyonu
        passwordError.value = password.value.isEmpty() || password.value.length < 6

        return !emailError.value && !passwordError.value
    }

    // Email değişimi
    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    // Password değişimi
    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }
}
