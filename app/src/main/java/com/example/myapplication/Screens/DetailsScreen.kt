package com.example.myapplication.Composable.Screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.VM.DetailsViewModel
import com.example.myapplication.VM.WeatherState
import com.example.myapplication.network.ForecastItem
import java.text.SimpleDateFormat
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun PreviewDetails(){
    DetailsScreen(rememberNavController())
}

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
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            when (weatherState) {
                is WeatherState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is WeatherState.Success -> {
                    val forecast = weatherState.data.list
                    // Верхняя половина
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxSize(0.5f)
                            .padding(top = 16.dp)
                    ) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                        ) {
                            items(forecast) { item ->
                                WeatherItem(item)
                            }
                        }
                    }
                }

                is WeatherState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = weatherState.message,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun WeatherItem(forecast: ForecastItem) {
    val formattedTime = remember(forecast.dt_txt) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d LLLL HH:mm", Locale("ru"))
        try {
            inputFormat.parse(forecast.dt_txt)?.let { outputFormat.format(it) } ?: forecast.dt_txt
        } catch (e: Exception) {
            forecast.dt_txt
        }
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(width = 160.dp, height = 220.dp)
            .border(BorderStroke(2.dp, Color.White), shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = Color.White
            )
            Text(
                text = forecast.weather[0].description,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = Color.White
            )
            Text(
                text = "Темп: ${forecast.main.temperature}°C",
                fontSize = 14.sp,
                color = Color.White
            )
            Text(
                text = "Влажн: ${forecast.main.humidity}%",
                fontSize = 14.sp,
                color = Color.White
            )

            val iconUrl = "https://openweathermap.org/img/wn/${forecast.weather[0].icon}.png"
            Log.d("WeatherItem", "Loading image: $iconUrl")

            AsyncImage(
                model = iconUrl,
                contentDescription = "Weather icon",
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
            )
        }
    }
}

