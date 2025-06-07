package com.luci.aeris.data.remote;
import com.luci.aeris.domain.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface WeatherApiService {
    @GET("{location}")
    suspend fun getWeather(
        @Path("location") location: String,
        @Query("unitGroup") unitGroup: String = "metric",
        @Query("include") include: String = "current,days",
        @Query("key") apiKey: String,
        @Query("contentType") contentType: String = "json"
    ): WeatherResponse
}
