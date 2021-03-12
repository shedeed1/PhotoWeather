package com.robusta.photoweather.api

import com.robusta.photoweather.BuildConfig
import com.robusta.photoweather.data.response.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/find")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("cnt") cnt: Int = 1,
        @Query("appid") appId: String = BuildConfig.OPENWEATHERMAP_ACCESS_KEY
    ): Response<WeatherResponse>

}