package com.luci.aeris.di

import com.luci.aeris.data.remote.WeatherApiService
import com.luci.aeris.data.repository.WeatherRepositoryImpl
import com.luci.aeris.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApi(): WeatherApiService {
        return Retrofit.Builder()
            .baseUrl("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    @Provides
    fun provideRepository(api: WeatherApiService): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }
}
