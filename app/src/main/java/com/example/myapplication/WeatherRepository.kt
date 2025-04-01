package com.example.myapplication

import android.util.Log
import com.example.myapplication.network.WeatherApi
import com.example.myapplication.network.WeatherResponse
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi) {
    suspend fun getWeather(city: String, apiKey: String): WeatherResponse {
        val data = api.getCurrentWeather(city, apiKey)
        Log.d("JSON", data.toString())
        return data
    }
}
