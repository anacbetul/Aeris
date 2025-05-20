package com.luci.aeris.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.yourapp.utils.SharedPrefRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ThemeViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val sharedPref = SharedPrefRepository.getInstance(application.applicationContext)

    private val _isDarkTheme = MutableStateFlow(sharedPref.isDarkModeEnabled())
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        val newValue = !_isDarkTheme.value
        _isDarkTheme.value = newValue
        sharedPref.setDarkModeEnabled(newValue)
    }
}
