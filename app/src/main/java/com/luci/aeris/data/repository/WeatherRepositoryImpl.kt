package com.luci.aeris.data.repository

import com.luci.aeris.data.mapper.toDomainList
import com.luci.aeris.data.mapper.toWeather
import com.luci.aeris.data.remote.WeatherApiService
import com.luci.aeris.data.remote.models.WeatherDto
import com.luci.aeris.domain.model.Weather
import com.luci.aeris.domain.model.WeatherResponse
import com.luci.aeris.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService, private val apiKey: String
) : WeatherRepository {
    //    override suspend fun getWeather(): List<Weather> {
//        val response = api.getWeather(apiKey = apiKey)
//
//        return response.toDomainList()
//    }
    private val _weatherState = MutableStateFlow<List<Weather>>(emptyList())
    private val weatherState: StateFlow<List<Weather>> = _weatherState
    override suspend fun getWeatherResponse(location: String, unitGroup: String): WeatherResponse {
        val response = api.getWeather(location = location, unitGroup = unitGroup, apiKey = apiKey)
        val domainDays = response.days.mapNotNull { dto: WeatherDto? -> dto?.toWeather(location)}

        _weatherState.value = domainDays

        return WeatherResponse(
            resolvedAddress = response.resolvedAddress,
            days = response.days.map { it },
            currentConditions = response.currentConditions
        )
    }
    override fun getWeatherState(): StateFlow<List<Weather>> = weatherState
}
