package com.robusta.photoweather.di

import com.robusta.photoweather.api.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

class NetworkModule {
    @InstallIn(SingletonComponent::class)
    @Module
    class NetworkModule {
        @Singleton
        @Provides
        fun provideWeatherService(): WeatherService {
            return WeatherService.create()
        }
    }
}