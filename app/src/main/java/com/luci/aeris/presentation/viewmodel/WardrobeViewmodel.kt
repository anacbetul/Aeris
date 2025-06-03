package com.luci.aeris.presentation.viewmodel

import Clothes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.luci.aeris.domain.repository.FirestoreUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
class WardrobeViewmodel(
    private val repository: FirestoreUserRepository = FirestoreUserRepository()
) : ViewModel() {

    private val _userClothes = MutableStateFlow<List<Clothes>>(emptyList())
    val userClothes: StateFlow<List<Clothes>> = _userClothes

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val clothesByCategory: StateFlow<Map<String, List<Clothes>>> =
        userClothes.map { clothesList ->
            clothesList.groupBy { it.type.ifEmpty { "Uncategorized" } }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    init {
        fetchUserClothes()
    }

    fun fetchUserClothes() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getClothesForCurrentUser()
            result.onSuccess { clothesList ->
                _userClothes.value = clothesList
            }.onFailure {
                _userClothes.value = emptyList()
                // Hatalar loglanabilir
            }
            _isLoading.value = false
        }
    }

    fun refresh() = fetchUserClothes()
}
