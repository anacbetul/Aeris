package com.luci.aeris.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luci.aeris.data.mapper.toCurrentCondition
import com.luci.aeris.data.mapper.toWeather
import com.luci.aeris.domain.model.CurrentConditions
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

    private val _selectedDay = MutableStateFlow<Weather?>(null)
    val selectedDay: StateFlow<Weather?> = _selectedDay

    private val _currentWeather = MutableStateFlow<CurrentConditions?>(null)
    val currentWeather: StateFlow<CurrentConditions?> = _currentWeather

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    fun selectDay(weather: Weather) {
        _selectedDay.value = weather
    }

    init{
        loadWeather()
    }
    fun loadWeather() {
        viewModelScope.launch {
            _isLoading.value = true
            val response = repository.getWeatherResponse()

            val resolvedAddress = response.resolvedAddress ?: ""
            val current = response.currentConditions?.toCurrentCondition(resolvedAddress)
            _currentWeather.value = current
            val weatherList = response.days.mapNotNull { it?.toWeather(resolvedAddress) }.toMutableList()

            current?.let {
                weatherList.removeAt(0)
                weatherList.add(0, it.toWeather())
            }
            _weatherState.value = weatherList
            _selectedDay.value = weatherList.firstOrNull()
            _isLoading.value = false
        }
    }

}
