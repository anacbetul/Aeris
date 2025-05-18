package com.luci.aeris.presentation.ui

import android.R.attr.text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.luci.aeris.presentation.viewmodel.WeatherViewModel



@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val weatherList by viewModel.weatherState.collectAsState()

    if (weatherList.isEmpty()) {
        // Veri henüz yüklenmemişse göster
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(weatherList.size - (weatherList.size - weatherList.size)) { weather ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Date: ${weatherList[weather].datetime}", style = MaterialTheme.typography.h6)
                        Text(text = "Temp: ${weatherList[weather].temp}°C")
                        Text(text = "Max: ${weatherList[weather].tempmax}°C, Min: ${weatherList[weather].tempmin}°C")
                        Text(text = "Condition: ${weatherList[weather].description}")
                        Text(text = "Humidity: ${weatherList[weather].humidity}%")
                        Text(text = "Wind: ${weatherList[weather].windspeed} km/h")
                        Text(text = "UV Index: ${weatherList[weather].uvindex}")
                        Text(text = "Location: ${weatherList[weather].resolvedAddress}")
                    }
                }
            }
        }
    }
}
