package com.luci.aeris.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luci.aeris.domain.model.Weather
import com.luci.aeris.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherState = MutableStateFlow<List<Weather>>(emptyList())
    val weatherState: StateFlow<List<Weather>> = _weatherState.asStateFlow()

    init {
        viewModelScope.launch {
            _weatherState.value = repository.getWeather()
        }
    }
}
