package com.example.myapplication.VM

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.WeatherRepository
import com.example.myapplication.network.WeatherForecastResponce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherState = mutableStateOf<WeatherState>(WeatherState.Loading)
    val weatherState get() = _weatherState // Теперь корректно

    private val apiKey = "5afd49dd964081b301b9d355f55c5e8b"

    fun fetchWeather(lat: Double, lon: Double) {
        Log.d("DetailsViewModel", "Fetching weather for lat=$lat, lon=$lon")

        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            try {
                val result = repository.getWeatherForecast(lat, lon, apiKey)

                if (result.isSuccess) {
                    result.getOrNull()?.let { response ->
                        Log.d("DetailsViewModel", "Weather data received: $response")
                        _weatherState.value = WeatherState.Success(response)
                    } ?: run {
                        Log.e("DetailsViewModel", "Response is null")
                        _weatherState.value = WeatherState.Error("No data received")
                    }
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Unknown error"
                    Log.e("DetailsViewModel", "Error fetching weather: $error")
                    _weatherState.value = WeatherState.Error(error)
                }
            } catch (e: Exception) {
                Log.e("DetailsViewModel", "Exception occurred: ${e.localizedMessage}", e)
                _weatherState.value = WeatherState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}


sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val data: WeatherForecastResponce) : WeatherState()
    data class Error(val message: String) : WeatherState()
}
