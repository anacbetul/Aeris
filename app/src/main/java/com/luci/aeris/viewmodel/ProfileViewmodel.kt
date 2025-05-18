package com.luci.aeris.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luci.aeris.constants.NavigationRoutes
import com.luci.aeris.domain.repository.FirebaseAuthRepository
import com.luci.aeris.utils.Navigator
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: FirebaseAuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    fun onSignOut(navigator: Navigator) {
        viewModelScope.launch {
            authRepository.signOut()
            println("asdada")
            navigator.navigateAndClearBackStack(NavigationRoutes.Login)
        }
    }
}
