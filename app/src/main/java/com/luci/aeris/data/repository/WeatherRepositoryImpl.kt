package com.luci.aeris.data.repository

import com.luci.aeris.data.mapper.toDomainList
import com.luci.aeris.data.remote.WeatherApiService
import com.luci.aeris.domain.model.Weather
import com.luci.aeris.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val api: WeatherApiService
) : WeatherRepository {
    override suspend fun getWeather(): List<Weather> {
        val response = api.getWeather() // Retrofit çağrısı
        return response.toDomainList()
    }
}
