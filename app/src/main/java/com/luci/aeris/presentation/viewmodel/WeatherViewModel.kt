package com.luci.aeris.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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

    private val _location = MutableStateFlow("Istanbul") // Default location
    val location = _location.asStateFlow()

    private val _currentLocation = MutableStateFlow<String?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    private val _unitGroup = MutableStateFlow<String>("metric")
    val unitGroup = _unitGroup.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun fetchCurrentLocation(latitude: Double, longitude: Double) {
        _currentLocation.value = "$latitude,$longitude"
    }

    fun selectDay(weather: Weather) {
        _selectedDay.value = weather
    }

    fun changeLocation(newLocation: String) {
        _location.value = newLocation
    }

    init {
        loadWeather(location.value, unitGroup.value)
    }

    fun loadWeather(location: String, unitGroup: String, useCurrentLocation: Boolean = false) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val queryLocation = if (useCurrentLocation && !_currentLocation.value.isNullOrEmpty()) {
                    _currentLocation.value!!
                } else {
                    location
                }
                val response = repository.getWeatherResponse(queryLocation, unitGroup)
                val resolvedAddress = response.resolvedAddress ?: ""
                val current = response.currentConditions?.toCurrentCondition(resolvedAddress)
                _currentWeather.value = current
                val weatherList =
                    response.days.mapNotNull { it?.toWeather(resolvedAddress) }.toMutableList()

                current?.let {
                    weatherList.removeAt(0)
                    weatherList.add(0, it.toWeather())
                }
                _weatherState.value = weatherList
                _selectedDay.value = weatherList.firstOrNull()
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("WeatherVM", "Error loading weather", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

}
