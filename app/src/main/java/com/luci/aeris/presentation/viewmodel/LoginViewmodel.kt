package com.luci.aeris.presentation.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.luci.aeris.utils.constants.StringConstants
import com.luci.aeris.domain.model.User
import com.luci.aeris.domain.model.UserCredentials
import com.luci.aeris.domain.repository.FirebaseAuthRepository
import com.luci.aeris.domain.repository.FirestoreUserRepository
import kotlinx.coroutines.launch

class LoginViewModel (
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreUserRepository
): ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var emailError by mutableStateOf(false)
    var passwordError by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf<Boolean?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var userCredentials by mutableStateOf<UserCredentials?>(null)
    var isLoadingGoogle by mutableStateOf(false)
    // Email ve password doğrulama fonksiyonu
    fun isFormValid(): Boolean {
        // Email validasyonu
        emailError = email.value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.value).matches()

        // Password validasyonu
        passwordError = password.value.isEmpty() || password.value.length < 6

        return !emailError && !passwordError
    }

    // Email değişimi
    fun onEmailChange(newEmail: String) {
        email.value = newEmail
        emailError = !Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
    }

    // Password değişimi
    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
        passwordError = password.value.length < 6
    }
     fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

     fun isValidPassword(password: String): Boolean {
        return password.length >= 6 // örnek kural: min 6 karakter
    }

    fun login(){
        if (!isFormValid()) return

        isLoading = true
        isSuccess = null
        errorMessage = null

        viewModelScope.launch {
            val result = authRepository.signInWithEmail(email.value, password.value)
            isLoading = false
            result.onSuccess { credentials ->
                userCredentials = credentials
                isSuccess = true
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
            val result = authRepository.sigInGoogle(account)
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
            password=password.value
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
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(authRepository, firestoreRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
