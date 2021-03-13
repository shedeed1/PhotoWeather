package com.robusta.photoweather.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.robusta.photoweather.api.RetrofitService
import com.robusta.photoweather.api.WeatherApi
import com.robusta.photoweather.data.response.WeatherResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class WeatherRepository {

    private val weatherService: WeatherApi

    init {
        weatherService =
            RetrofitService.createService(WeatherApi::class.java)
    }

    fun getWeather(lat: Double,long: Double): MutableLiveData<WeatherResponse> {
        val weatherData = MutableLiveData<WeatherResponse>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = weatherService.getWeather(lat,long)
                try {
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                weatherData.postValue(it)
                            }
                        } else {
                            weatherData.postValue(null)
                            Log.e("REQUEST", "Exception")
                        }
                    }
                } catch (e: HttpException) {
                    weatherData.postValue(null)
                    Log.e("REQUEST", "Exception ${e.message}")
                } catch (e: Throwable) {
                    weatherData.postValue(null)
                    Log.e("REQUEST", "Ooops: Something else went wrong")
                }
            }
            catch (e: Throwable)
            {
                Log.i("REQUEST",e.localizedMessage)
            }
        }
        return weatherData
    }

    companion object {

        private var weatherRepository: WeatherRepository? = null

        val instance: WeatherRepository
            get() {
                if (weatherRepository == null) {
                    weatherRepository =
                        WeatherRepository()
                }
                return weatherRepository as WeatherRepository
            }
    }
}