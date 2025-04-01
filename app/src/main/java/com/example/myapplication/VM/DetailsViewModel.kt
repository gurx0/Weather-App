package com.example.myapplication.VM

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor() : ViewModel() {
    private val _dailyWeather = MutableStateFlow("Sunny, 25°C")
    val dailyWeather: StateFlow<String> = _dailyWeather

    private val _weeklyWeather = MutableStateFlow(listOf("Mon: Rainy", "Tue: Cloudy", "Wed: Sunny"))
    val weeklyWeather: StateFlow<List<String>> = _weeklyWeather
}