package com.luci.aeris.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.luci.aeris.domain.model.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseOutfitViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

    private val _recommendedCategories = MutableStateFlow<List<String>>(emptyList())
    val recommendedCategories: StateFlow<List<String>> = _recommendedCategories

    fun observeSelectedDay(selectedDay: StateFlow<Weather?>) {
        viewModelScope.launch {
            selectedDay.collectLatest { weather ->
                weather?.let {
                    val categories = getCategoriesForTemperature(it.temp)
                    _recommendedCategories.value = categories
                }
            }
        }
    }

//    init {
//        viewModelScope.launch {
//            weatherViewModel.selectedDay.collectLatest { weather ->
//                weather?.let {
//                    val categories = getCategoriesForTemperature(it.temp)
//                    _recommendedCategories.value = categories
//                }
//            }
//        }
//    }
    fun updateRecommendations(temp: Double) {
        val categories = getCategoriesForTemperature(temp)
        _recommendedCategories.value = categories
    }

    private fun getCategoriesForTemperature(temp: Double): List<String> {
        return when {
            temp >= 30 -> listOf(
                "Caps",
                "Sunglasses",
                "T-shirt",
                "Short",
                "Skirt",
                "Dress",
                "Sandals"
            )

            temp in 20.0..29.9 -> listOf(
                "Caps",
                "Sunglasses",
                "T-shirt",
                "Shirt",
                "Short",
                "Skirt",
                "Dress",
                "Sports Shoes",
                "Casual Shoes",
            )

            temp in 10.0..19.9 -> listOf(
                "Jacket",
                "Hoodie",
                "Sweater",
                "Tracksuit",
                "Pant",
                "Casual Shoes",
                "Sports Shoes",
                "Formal Shoes"
            )

            temp in 0.0..9.9 -> listOf(
                "Winter Beret",
                "Coat",
                "Denimcoat",
                "Jacket",
                "Raincoat",
                "Boots"
            )

            else -> listOf(
                "Winter Beret",
                "Coat",
                "Denimcoat",
                "Raincoat",
                "Boots"
            )
        }
    }
}
