package com.luci.aeris.domain.repository

import com.luci.aeris.domain.model.Weather
import com.luci.aeris.domain.model.WeatherResponse
import kotlinx.coroutines.flow.StateFlow


interface WeatherRepository {
    //suspend fun getWeather(): List<Weather>
    suspend fun getWeatherResponse(location:String, unitGroup: String): WeatherResponse
    fun getWeatherState(): StateFlow<List<Weather>>
}

