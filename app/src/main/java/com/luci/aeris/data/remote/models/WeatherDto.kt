package com.luci.aeris.data.remote.models

data class WeatherDto(
    val datetime: String?,
    val tempmax: Double?,
    val tempmin: Double?,
    val temp: Double?,
    val feelslike: Double?,
    val humidity: Double?,
    val windspeed: Double?,
    val uvindex: Double?,
    val icon: String?,
    val description: String?,
    val conditions: String?,
)

data class CurrentConditionsDto(
    val datetime: String?,
    val tempmax: Double?,
    val tempmin: Double?,
    val temp: Double?,
    val feelslike: Double?,
    val humidity: Double?,
    val windspeed: Double?,
    val uvindex: Double?,
    val icon: String?,
    val description: String?,
    val conditions: String?,
    val resolvedAddress: String?
)
