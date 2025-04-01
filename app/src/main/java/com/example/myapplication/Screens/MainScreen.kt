package com.example.myapplication.Composable.Screens

import com.example.myapplication.VM.MainViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val weatherState = viewModel.weatherState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWeather("Москва")
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
    val weather = weatherState.value

        weather?.let{
            Text("Город: ${it.cityName}")
            Text("Температура: ${it.main.temperature}°C")
            Text("Влажность: ${it.main.humidity}%")
            Text("Ветер: ${it.wind.speed} м/с")
            Text("Описание: ${it.weather.firstOrNull()?.description ?: "Нет данных"}")
        } ?: Text("Загрузка погоды...")
    }
}

