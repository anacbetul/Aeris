package com.luci.aeris.data.repository

import com.luci.aeris.data.mapper.toDomainList
import com.luci.aeris.data.remote.WeatherApiService
import com.luci.aeris.domain.model.Weather
import com.luci.aeris.domain.model.WeatherResponse
import com.luci.aeris.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService, private val apiKey: String
) : WeatherRepository {
//    override suspend fun getWeather(): List<Weather> {
//        val response = api.getWeather(apiKey = apiKey)
//
//        return response.toDomainList()
//    }

    override suspend fun getWeatherResponse(): WeatherResponse {
        val response = api.getWeather(apiKey = apiKey)
        return WeatherResponse(
            resolvedAddress = response.resolvedAddress,
            days = response.days.map { it },
            currentConditions = response.currentConditions
        )
    }

}
