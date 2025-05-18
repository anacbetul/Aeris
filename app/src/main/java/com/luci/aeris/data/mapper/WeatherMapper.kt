package com.luci.aeris.data.mapper

import androidx.compose.ui.text.resolveDefaults
import com.luci.aeris.data.remote.models.WeatherDto
import com.luci.aeris.domain.model.Weather
import com.luci.aeris.domain.model.WeatherResponse

fun WeatherResponse.toDomainList(): List<Weather> {
    return days.map { it.toDomain(resolvedAddress) }
}

fun WeatherDto.toDomain(resolvedAddress: String): Weather {
    return Weather(
        datetime = datetime ?: "",
        tempmax = tempmax ?: 0.0,
        tempmin = tempmin ?: 0.0,
        temp = temp ?: 0.0,
        humidity = humidity ?: 0.0,
        windspeed = windspeed ?: 0.0,
        uvindex = uvindex ?: 0.0,
        icon = icon ?: "",
        description = description ?: "",
        conditions = conditions ?: "",
        resolvedAddress = resolvedAddress
    )
}

