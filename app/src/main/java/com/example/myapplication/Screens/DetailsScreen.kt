package com.example.myapplication.Composable.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.Button

@Composable
fun DetailsScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { navController.navigate("main") }) {
            Text("Назад")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("Погода на весь день", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text("Погода на неделю (скролл)", fontSize = 18.sp)
    }
}
