package com.luci.aeris.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.luci.aeris.constants.NavigationRoutes
import com.luci.aeris.domain.model.User
import com.luci.aeris.domain.repository.FirebaseAuthRepository
import com.luci.aeris.domain.repository.FirestoreUserRepository
import com.luci.aeris.utils.Navigator
import com.yourapp.utils.SharedPrefRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
     val authRepository: FirebaseAuthRepository = FirebaseAuthRepository(),
     val firestoreUserRepository: FirestoreUserRepository = FirestoreUserRepository()
) : AndroidViewModel(application) {

    var user by mutableStateOf<User?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadUser(uid: String) {
        viewModelScope.launch {
            isLoading = true
            firestoreUserRepository.getUser(uid)
                .onSuccess { userData ->
                    user = userData
                }
                .onFailure {
                    Log.e("ProfileViewModel", "Kullanıcı verisi alınamadı: ${it.message}")
                }
            isLoading = false
        }
    }

    fun onSignOut(navigator: Navigator) {
        viewModelScope.launch {
            authRepository.signOut()
            navigator.navigateAndClearBackStack(NavigationRoutes.Login)
        }
    }
}
