package com.luci.aeris.domain.repository

import com.luci.aeris.domain.model.Weather


interface WeatherRepository {
    suspend fun getWeather(): List<Weather>
}
