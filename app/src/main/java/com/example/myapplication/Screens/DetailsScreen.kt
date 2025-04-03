package com.example.myapplication.Composable.Screens


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.VM.DetailsViewModel
import com.example.myapplication.VM.WeatherState
import com.example.myapplication.network.ForecastItem

@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: DetailsViewModel = hiltViewModel(),
    ) {
    val weatherState = viewModel.weatherState.value
    val lat = 55.7558
    val lon = 37.6173

    LaunchedEffect(Unit) {
        viewModel.fetchWeather(lat, lon)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box {
            when (weatherState) {
                is WeatherState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is WeatherState.Success -> {
                    val forecast = weatherState.data.list
                    LazyColumn {
                        items(forecast) { item ->
                            WeatherItem(item)
                        }
                    }
                }
                is WeatherState.Error -> {
                    Text(
                        text = weatherState.message,
                        color = Color.Red,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherItem(forecast: ForecastItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Время: ${forecast.dt_txt}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Температура: ${forecast.main.temperature}°C", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Погода: ${forecast.weather[0].description}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Влажность: ${forecast.main.humidity}%", style = MaterialTheme.typography.bodyMedium)

            val iconUrl = "http://openweathermap.org/img/wn/${forecast.weather[0].icon}.png"
            Log.d("WeatherItem", "Loading image: $iconUrl")

            AsyncImage(
                model = iconUrl,
                contentDescription = "Weather icon",
                modifier = Modifier.size(50.dp)
            )
        }
    }
}