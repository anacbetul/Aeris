package com.luci.aeris.viewmodel


import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var emailError by mutableStateOf(false)
    var passwordError by mutableStateOf(false)
    var confirmPasswordError by mutableStateOf(false)

    fun onNameChange(newValue: String) {
        name = newValue
    }

    fun onEmailChange(newValue: String) {
        email = newValue
        emailError = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
        passwordError = password.length < 6
    }

    fun onConfirmPasswordChange(newValue: String) {
        confirmPassword = newValue
        confirmPasswordError = confirmPassword != password
    }

    fun isFormValid(): Boolean {
        emailError = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        passwordError = password.length < 6
        confirmPasswordError = confirmPassword != password

        return !emailError && !passwordError && !confirmPasswordError
    }
}
