package com.example.myapplication.Composable.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.VM.MainViewModel
import java.time.LocalDate

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val weatherState = viewModel.weatherState.collectAsState()
    val weather = weatherState.value

    LaunchedEffect(Unit) {
        viewModel.loadWeather("Москва")
    }

    Box(Modifier.fillMaxSize()) {
        // Город в левом верхнем углу
        Row(
            Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.Place, contentDescription = "place", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Москва", color = Color.White, fontSize = 18.sp)
        }

        // Полупрозрачное окно с границей
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .wrapContentHeight()
                .align(Alignment.Center)
                .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp))
                .border(BorderStroke(2.dp, Color.White), shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                weather?.let {
                    Text("Сегодня, ${LocalDate.now().month} ${LocalDate.now().dayOfMonth}", color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Text("${it.main.temperature}°", fontSize = 40.sp, color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Text(it.weather.firstOrNull()?.description ?: "Нет данных", color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(R.drawable.wind), contentDescription = "wind", tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Ветер | ${it.wind.speed} м/c", color = Color.White)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(R.drawable.hum), contentDescription = "humidity", tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Влажность | ${it.main.humidity} %", color = Color.White)
                    }
                }
            }
        }
    }
}
