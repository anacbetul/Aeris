package com.luci.aeris.data.remote;
import com.luci.aeris.data.remote.models.WeatherDto
import com.luci.aeris.domain.model.WeatherResponse
import retrofit2.http.GET

interface WeatherApiService {
    @GET("bursa?unitGroup=metric&include=current%2Cdays&key=GCMQDJJFUGELFBYGTQLMVBDND&contentType=json")
    suspend fun getWeather(): WeatherResponse
}
