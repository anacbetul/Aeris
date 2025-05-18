package com.luci.aeris.data.mapper

import com.google.android.play.integrity.internal.f
import com.luci.aeris.data.remote.models.CurrentConditionsDto
import com.luci.aeris.data.remote.models.WeatherDto
import com.luci.aeris.domain.model.CurrentConditions
import com.luci.aeris.domain.model.Weather
import com.luci.aeris.domain.model.WeatherResponse
import kotlin.String

fun WeatherResponse.toDomainList(): List<Weather> {
    val address = resolvedAddress ?: ""
    return days.filterNotNull().map { it.toWeather(address) }
}

fun WeatherDto.toWeather(resolvedAddress: String): Weather {
    return Weather(
        datetime = datetime ?: "",
        tempmax = tempmax ?: 0.0,
        tempmin = tempmin ?: 0.0,
        temp = temp ?: 0.0,
        feelslike = feelslike ?: 0.0,
        humidity = humidity ?: 0.0,
        windspeed = windspeed ?: 0.0,
        uvindex = uvindex ?: 0.0,
        icon = icon ?: "",
        description = description ?: "",
        conditions = conditions ?: "",
        resolvedAddress = resolvedAddress
    )
}

fun CurrentConditionsDto.toCurrentCondition(resolvedAddress: String): CurrentConditions {
    return CurrentConditions(
        datetime = datetime ?: "",
        tempmax = 0.0,
        tempmin = 0.0,
        temp = temp ?: 0.0,
        feelslike = feelslike ?: 0.0,
        humidity = humidity ?: 0.0,
        windspeed = windspeed ?: 0.0,
        uvindex = uvindex ?: 0.0,
        icon = icon ?: "",
        conditions = conditions ?: "",
        description = "",
        resolvedAddress = resolvedAddress
    )
}
fun CurrentConditions.toWeather(): Weather {
    return Weather(
        datetime = datetime,
        tempmax = temp,
        tempmin = temp,
        temp = temp,
        feelslike = feelslike,
        humidity = humidity,
        windspeed = windspeed,
        uvindex = uvindex,
        icon = icon,
        description = "",
        conditions = conditions,
        resolvedAddress = resolvedAddress
    )
}
