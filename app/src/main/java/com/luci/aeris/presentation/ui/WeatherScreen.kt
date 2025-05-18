package com.luci.aeris.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luci.aeris.presentation.viewmodel.WeatherViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val weatherList by viewModel.weatherState.collectAsState()
    val selectedWeather by viewModel.selectedDay.collectAsState()
    val currentWeather by viewModel.currentWeather.collectAsState()

    if (weatherList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(weatherList.size) { index ->
                val weather = weatherList[index]
                val isSelected = selectedWeather == weather

                Card(
                    modifier = Modifier
                        .clickable { viewModel.selectDay(weather) }
                        .padding(4.dp),
                    elevation = (4.dp),

                    ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = (weather.datetime))
                        Text(text = "${weather.temp}°C")
                    }
                }
            }
        }

    }
    selectedWeather?.let { weather ->
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Tarih: ${(weather.datetime)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text("Sıcaklık: ${weather.temp}°C")
            Text("Durum: ${weather.conditions}")
            Text("Konum: ${weather.resolvedAddress}")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(apiDate: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val date = LocalDate.parse(apiDate, inputFormatter)
    return date.format(outputFormatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(apiDate: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val outputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    val date = LocalDate.parse(apiDate, inputFormatter)
    return date.format(outputFormatter)
}