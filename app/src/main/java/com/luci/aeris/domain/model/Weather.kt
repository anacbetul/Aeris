package com.luci.aeris.domain.model

import com.luci.aeris.data.remote.models.WeatherDto

data class WeatherResponse(
    val resolvedAddress: String,
    val days: List<WeatherDto>
)

data class Weather(
    val datetime: String,
    val tempmax: Double,
    val tempmin: Double,
    val temp: Double,
    val humidity: Double,
    val windspeed: Double,
    val uvindex: Double,
    val icon: String,
    val description: String,
    val conditions: String,
    val resolvedAddress: String
)