package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Composable.Screens.DetailsScreen
import com.example.myapplication.Composable.Screens.MainScreen
import com.example.myapplication.Composable.Screens.MapScreen
import com.example.myapplication.Composable.Screens.SearchScreen
import com.example.myapplication.VM.DetailsViewModel
import com.example.myapplication.VM.MainViewModel
import com.example.myapplication.VM.SearchViewModel
import com.example.myapplication.ui.theme.PrimaryDefault
import com.example.myapplication.ui.theme.TextDark

sealed class Screen(val route: String, val icon: ImageVector) {
    object Main : Screen("main", Icons.Default.Home)
    object Details : Screen("details", Icons.Default.Info)
    object Search : Screen("search", Icons.Default.Settings)
    object Map : Screen("map", Icons.Default.LocationOn)
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBar(navController: NavController = rememberNavController()) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    val screens = listOf(
        Screen.Main,
        Screen.Details,
        Screen.Search,
        Screen.Map
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        screens.forEach { screen ->
            val isSelected = currentRoute == screen.route

            Card(
                modifier = Modifier
                    .height(48.dp)
                    .width(56.dp),
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) PrimaryDefault else Color.Transparent
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.route,
                        modifier = Modifier.size(24.dp),
                        tint = if (isSelected) Color.White else TextDark
                    )
                    Text(
                        text = screen.route,
                        color = if (isSelected) Color.White else TextDark
                    )
                }
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Main.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Main.route) {
                val viewModel: MainViewModel = hiltViewModel()
                MainScreen(navController)
            }
            composable(Screen.Details.route) {
                val viewModel: DetailsViewModel = hiltViewModel()
                DetailsScreen(navController)
            }
            composable(Screen.Search.route) {
                val viewModel: SearchViewModel = hiltViewModel()
                SearchScreen(navController)
            }
            composable(Screen.Map.route) {
                MapScreen(navController)
            }
        }
    }
}

