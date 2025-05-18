package com.luci.aeris.data.repository

import com.luci.aeris.data.mapper.toDomainList
import com.luci.aeris.data.remote.WeatherApiService
import com.luci.aeris.domain.model.Weather
import com.luci.aeris.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService, private val apiKey: String
) : WeatherRepository {
    override suspend fun getWeather(): List<Weather> {
        val response = api.getWeather(apiKey=apiKey) // Retrofit çağrısı

        return response.toDomainList()
    }
}
