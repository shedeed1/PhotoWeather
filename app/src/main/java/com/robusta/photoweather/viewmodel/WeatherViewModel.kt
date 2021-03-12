package com.robusta.photoweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robusta.photoweather.data.WeatherRepository
import com.robusta.photoweather.data.response.WeatherResponse
import cz.msebera.android.httpclient.client.cache.Resource

class WeatherViewModel(lat: Double, long: Double) : ViewModel() {
    private val weather: MutableLiveData<WeatherResponse>
    private val weatherRepository: WeatherRepository = WeatherRepository.instance

    init {
        weather = weatherRepository.getWeather(lat,long)
    }

    fun getWeather() : MutableLiveData<WeatherResponse>
    {
        return weather
    }
}