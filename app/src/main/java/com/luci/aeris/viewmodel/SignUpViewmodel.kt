package com.luci.aeris.viewmodel

import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.luci.aeris.constants.StringConstants
import com.luci.aeris.domain.model.User
import com.luci.aeris.domain.model.UserCredentials
import com.luci.aeris.domain.repository.FirebaseAuthRepository
import com.luci.aeris.domain.repository.FirestoreUserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreUserRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var emailError by mutableStateOf(false)
    var passwordError by mutableStateOf(false)
    var confirmPasswordError by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
    var isLoadingGoogle by mutableStateOf(false)
    var isSuccess by mutableStateOf<Boolean?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    var userCredentials by mutableStateOf<UserCredentials?>(null)


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

    private fun isFormValid(): Boolean {
        emailError = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        passwordError = password.length < 6
        confirmPasswordError = confirmPassword != password
        return !emailError && !passwordError && !confirmPasswordError
    }
    fun registerUser() {
        if (!isFormValid()) return

        isLoading = true
        isSuccess = null
        errorMessage = null

        viewModelScope.launch {
            val result = authRepository.registerWithEmail(email, password)
            isLoading = false
            result.onSuccess { credentials ->
                userCredentials = credentials
                isSuccess = true

                saveUserToFirestore(credentials)
            }.onFailure { error ->
                errorMessage = error.localizedMessage ?: StringConstants.somethingWentWrong
                isSuccess = false
            }
        }
    }
    fun signInWithGoogle(account: GoogleSignInAccount, fullNameFromGoogle: String?=null, callback: (Boolean, String?) -> Unit) {
        isLoadingGoogle = true
        isSuccess = null
        errorMessage = null

        viewModelScope.launch {
            val result = authRepository.registerWithGoogle(account)
            isLoadingGoogle = false
            result.onSuccess { credentials ->
                userCredentials = credentials
                val name = fullNameFromGoogle ?: account.displayName ?: StringConstants.guest
                saveUserToFirestore(credentials, name)
                callback(true, null)
            }.onFailure { error ->
                isSuccess = false
                errorMessage = error.localizedMessage ?: StringConstants.somethingWentWrongOnGoogle
                callback(false, errorMessage)
            }
        }
    }

    private fun saveUserToFirestore(credentials: UserCredentials, fullNameOverride: String? = null) {
        val user = User(
            uid = credentials.uid,
            fullName = fullNameOverride ?: StringConstants.guest,
            email = credentials.email ?: StringConstants.guestMail,
            deviceToken = null,
            password=password
        )

        viewModelScope.launch {
            val result = firestoreRepository.saveUser(user)
            result.onSuccess {
                isSuccess = true
            }.onFailure { error ->
                errorMessage = error.localizedMessage ?: StringConstants.failToCreateUser
                isSuccess = false
            }
        }
    }

    class Factory(
        private val authRepository: FirebaseAuthRepository,
        private val firestoreRepository: FirestoreUserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(authRepository, firestoreRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
