package com.luci.aeris.presentation.ui

import BodyText
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.luci.aeris.R
import com.luci.aeris.presentation.viewmodel.WeatherViewModel
import java.time.LocalDate
import java.time.LocalTime
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
                    elevation = if (isSelected) (12.dp) else (8.dp),
                    shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 12.dp),
                    backgroundColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (index == 0) {

                            BodyText(text = "Now", fontSize = 20.sp)
                        } else {
                            Row {
                                BodyText(text = (formatDay(weather.datetime)), fontSize = 20.sp)
                            }
                        }
                    }
                }
            }
        }

    }
    selectedWeather?.let { weather ->
        FrostedGlassCard {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BodyText(
                    text = "${weather.temp.toInt()}°C",
                    fontSize = 64.sp,
                    textColor = Color.White
                )
                WeatherIcon(weather.icon)
            }
            BodyText(weather.resolvedAddress)
            Spacer(modifier = Modifier.size(24.dp))
            if (weather == weatherList.first()) {

                BodyText("Feels like ${weather.feelslike.toInt()}°")
                BodyText(formatTime(weather.datetime), fontSize = 12.sp)
            } else {
                BodyText("${weather.tempmax.toInt()}°/ ${weather.tempmin.toInt()}° Feels like ${weather.feelslike.toInt()}°")
                BodyText(formatDate(weather.datetime), fontSize = 12.sp)

            }
        }
    }
}

@Composable
fun FrostedGlassCard(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
        //.background(MaterialTheme.colorScheme.primary) // dışarıdan transparan
    ) {
        // Arka plan: sadece bu blur olacak
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                .blur(20.dp) // sadece bu kutuya blur
        )

        // Ön plan: yazılar ve içerik blur'suz
        Column(
            modifier = Modifier
                .padding(16.dp),
            content = content
        )
    }
}

@Composable
fun WeatherIcon(iconName: String) {

    val iconResId = when (iconName) {
        "clear-day" -> R.drawable.clear_day
        "clear-night" -> R.drawable.clear_night
        "cloudy" -> R.drawable.cloudy
        "fog" -> R.drawable.fog
        "hail" -> R.drawable.hail
        "partly-cloudy-day" -> R.drawable.partly_cloudy_day
        "partly-cloudy-night" -> R.drawable.partly_cloudy_night
        "rain" -> R.drawable.rain
        "rain-snow" -> R.drawable.rain_snow
        "rain-snow-showers-day" -> R.drawable.rain_snow_showers_day
        "rain-snow-showers-night" -> R.drawable.rain_snow_showers_night
        "showers-day" -> R.drawable.showers_day
        "showers-night" -> R.drawable.showers_night
        "sleet" -> R.drawable.sleet
        "snow" -> R.drawable.snow
        "snow-showers-day" -> R.drawable.snow_showers_day
        "snow-showers-night" -> R.drawable.snow_showers_night
        "thunder" -> R.drawable.thunder
        "thunder-rain" -> R.drawable.thunder_rain
        "thunder-showers-day" -> R.drawable.thunder_showers_day
        "thunder-showers-night" -> R.drawable.thunder_showers_night
        "wind" -> R.drawable.wind
        else -> R.drawable.fog // Varsayılan ikon
    }

    Image(
        painter = painterResource(id = iconResId),
        contentDescription = null,
        modifier = Modifier.size(128.dp)
    )

}


@RequiresApi(Build.VERSION_CODES.O)
fun formatDay(apiDate: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("dd")

    val date = LocalDate.parse(apiDate, inputFormatter)
    return date.format(outputFormatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(apiDate: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val date = LocalTime.parse(apiDate, inputFormatter)
    return date.format(outputFormatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(apiDate: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val date = LocalDate.parse(apiDate, inputFormatter)
    return date.format(outputFormatter)
}
