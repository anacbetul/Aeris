package com.luci.aeris.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.luci.aeris.utils.constants.NavigationRoutes
import com.luci.aeris.domain.model.User
import com.luci.aeris.domain.repository.FirebaseAuthRepository
import com.luci.aeris.domain.repository.FirestoreUserRepository
import com.luci.aeris.utils.constants.StringConstants
import com.luci.aeris.utils.navigator.Navigator
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
                    Log.e(StringConstants.profileViewmodel, "${StringConstants.didnotFoundUserData} ${it.message}")
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

    fun deleteAccount(navigator: Navigator) {
        viewModelScope.launch {
            val currentUser = authRepository.currentUser
            val uid = currentUser?.uid ?: return@launch

            try {
                // 1. Firestore'dan sil
                firestoreUserRepository.deleteUser(uid).onFailure {
                    Log.e("DeleteAccount", "Firestore'dan silinemedi: ${it.message}")
                    return@launch
                }

                // 2. Firebase Auth'tan sil
                currentUser.delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("DeleteAccount", "Kullanıcı başarıyla silindi")
                        navigator.navigateAndClearBackStack(NavigationRoutes.Login)
                    } else {
                        Log.e("DeleteAccount", "Firebase Auth'tan silme başarısız: ${task.exception?.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("DeleteAccount", "Hesap silinirken hata oluştu: ${e.message}")
            }
        }
    }
    fun updateUserGender(newGender: String) {
        viewModelScope.launch {
            user?.let { currentUser ->
                val updatedUser = currentUser.copy(gender = newGender)
                firestoreUserRepository.saveUser(updatedUser)
                    .onSuccess {
                        user = updatedUser // Ekranda da güncellenmiş hali yansıtılsın
                        Log.d("ProfileViewModel", "Kullanıcı başarıyla güncellendi.")
                    }
                    .onFailure {
                        Log.e("ProfileViewModel", "Kullanıcı güncellenemedi: ${it.message}")
                    }
            }
        }
    }
}
