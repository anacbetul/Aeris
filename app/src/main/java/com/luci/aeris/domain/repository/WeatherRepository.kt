package com.luci.aeris.domain.repository

import com.luci.aeris.domain.model.Weather
import com.luci.aeris.domain.model.WeatherResponse


interface WeatherRepository {
    //suspend fun getWeather(): List<Weather>
    suspend fun getWeatherResponse(): WeatherResponse
}
