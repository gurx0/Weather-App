package com.example.myapplication.network

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("main") val main: MainInfo,
    @SerializedName("weather") val weather: List<WeatherInfo>,
    @SerializedName("wind") val wind: WindInfo,
    @SerializedName("name") val cityName: String
)

data class MainInfo(
    @SerializedName("temp") val temperature: Float,
    @SerializedName("humidity") val humidity: Int
)

data class WeatherInfo(
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class WindInfo(
    @SerializedName("speed") val speed: Float
)
