package com.example.myapplication

import android.util.Log
import com.example.myapplication.network.WeatherApi
import com.example.myapplication.network.WeatherForecastResponce
import com.example.myapplication.network.WeatherResponse
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi) {
    suspend fun getWeather(city: String, apiKey: String): WeatherResponse {
        val data = api.getCurrentWeather(city, apiKey)
        Log.d("JSON", data.toString())
        return data
    }

    suspend fun getWeatherForecast(lat: Double, lon: Double, apiKey: String): Result<WeatherForecastResponce> {
        return try {
            val response = api.getWeatherForecast(lat, lon, apiKey)
            Log.d("JSON",response.body().toString())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
