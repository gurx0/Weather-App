package com.example.myapplication.VM

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repo.WeatherRepository
import com.example.myapplication.network.WeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState: StateFlow<WeatherResponse?> = _weatherState

    private val apiKey = "5afd49dd964081b301b9d355f55c5e8b"

    fun loadWeather(city: String) {
        viewModelScope.launch {
            try {
                val weather = repository.getWeather(city, apiKey)
                _weatherState.value = weather
            } catch (e: Exception) {
                Log.e("WeatherError", "Ошибка загрузки погоды", e)
            }
        }
    }
}
