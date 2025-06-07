package com.luci.aeris.presentation.ui

import BodyText
import HorizontalSpacer24
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.luci.aeris.R
import com.luci.aeris.presentation.viewmodel.WeatherViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.core.net.toUri

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val weatherList by viewModel.weatherState.collectAsState()
    val selectedWeather by viewModel.selectedDay.collectAsState()
    val currentWeather by viewModel.currentWeather.collectAsState()
    var location by remember { mutableStateOf("istanbul") }
    var unitGroup by remember { mutableStateOf("metric") }
    val showLocationSheet = remember { mutableStateOf(false) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var useCurrentLocation by remember { mutableStateOf(false) }
    val locationPermissionState =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val gifEnabledLoader = ImageLoader.Builder(context = context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()

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
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { viewModel.selectDay(weather) }
                        )
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

                            BodyText(
                                text = "Now",
                                fontSize = 20.sp,
                                textColor = MaterialTheme.colorScheme.tertiary
                            )
                        } else {
                            Row {
                                BodyText(
                                    text = (formatDay(weather.datetime)),
                                    fontSize = 20.sp,
                                    textColor = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                }
            }
        }

    }

    selectedWeather?.let { weather ->
        FrostedGlassCard(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            unitGroup = unitGroup,
            onUnitChange = { unitGroup = it },
            showLocationSheet = showLocationSheet.value,
            location = location,
            onReloadClick = { viewModel.loadWeather(location, unitGroup) },
            onShowLocationSheetChange = { showLocationSheet.value = it },
            onIconClick = {
                expanded = true
            }) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f) // Sol taraf
                ) {
                    var unitSymbol: String = "C"
                    if (unitGroup == "metric")
                        unitSymbol = "C"
                    if (unitGroup == "us")
                        unitSymbol = "F"
                    BodyText(
                        text = "${weather.temp.toInt()}°${unitSymbol}",
                        fontSize = 64.sp, textColor = MaterialTheme.colorScheme.tertiary
                    )
                    if (weather == weatherList.first()) {
                        BodyText(
                            "Feels like ${weather.feelslike.toInt()}°",
                            fontSize = 20.sp,
                            textColor = MaterialTheme.colorScheme.tertiary
                        )
                    } else {
                        BodyText(
                            "${weather.tempmax.toInt()}°/ ${weather.tempmin.toInt()}° \nFeels like ${weather.feelslike.toInt()}°",
                            fontSize = 20.sp, textColor = MaterialTheme.colorScheme.tertiary
                        )

                    }
                }
                //WeatherIcon(weather.icon)

                val gifUri = getWeatherIconUri(weather.icon, context)

                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(gifUri)
                        .build(),
                    imageLoader = gifEnabledLoader
                )
                HorizontalSpacer24()
                Image(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(max = 130.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    painter = painter,
                    contentDescription = weather.conditions,
                    contentScale = ContentScale.Fit
                )


            }
            Spacer(modifier = Modifier.size(24.dp))
            BodyText(weather.resolvedAddress, textColor = MaterialTheme.colorScheme.tertiary)
            if (weather == weatherList.first()) {
                BodyText(
                    formatTime(weather.datetime),
                    fontSize = 12.sp,
                    textColor = MaterialTheme.colorScheme.tertiary
                )
            } else {
                BodyText(
                    formatDate(weather.datetime),
                    fontSize = 12.sp,
                    textColor = MaterialTheme.colorScheme.tertiary
                )

            }
        }

    }
    LaunchedEffect(useCurrentLocation) {
        if (useCurrentLocation) {
            if (locationPermissionState.status.isGranted) {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    if (loc != null) {
                        val latLngString = "${loc.latitude},${loc.longitude}"
                        location = latLngString
                        viewModel.changeLocation(location)
                        viewModel.loadWeather(location, unitGroup)
                        showLocationSheet.value = false
                    } else {
                        // Konum alınamadıysa kullanıcıyı bilgilendir veya hata yönetimi yap
                    }
                }
            } else {
                locationPermissionState.launchPermissionRequest()
            }
        }
    }
    if (showLocationSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showLocationSheet.value = false },
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)  // ekranın %70'i kadar yükseklik
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = location,
                    onValueChange = {
                        location = it
                        useCurrentLocation = false
                    },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        useCurrentLocation = true
                        showLocationSheet.value = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Use Current Location")
                }
                Button(
                    onClick = {
                        viewModel.changeLocation(location)
                        viewModel.loadWeather(location, unitGroup)
                        showLocationSheet.value = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Get Weather")
                }
            }
        }
    }
}


@Composable
fun FrostedGlassCard(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    unitGroup: String,
    showLocationSheet: Boolean,
    location: String,
    onUnitChange: (String) -> Unit,
    onReloadClick: () -> Unit,
    onIconClick: () -> Unit = {},
    onShowLocationSheetChange: (Boolean) -> Unit,
    content: @Composable ColumnScope.() -> Unit

) {

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
        //.background(MaterialTheme.colorScheme.primary) // dışarıdan transparan
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.White.copy(alpha = 0.5f))
                .blur(20.dp)
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.align(Alignment.End)) {
                Box {
                    IconButton(
                        onClick = {
                            onIconClick()
                            onExpandedChange(true)
                        },
                        modifier = Modifier
                        //.align(Alignment.TopEnd)
                        //.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Info",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { onExpandedChange(false) },
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                onExpandedChange(false)
                                onShowLocationSheetChange(true)
                            }
                        ) {
                            BodyText("Location", textColor = MaterialTheme.colorScheme.tertiary)
                        }
                        DropdownMenuItem(onClick = {
                            onUnitChange("metric")
                            onExpandedChange(false)
                            onReloadClick()
                        }) {
                            BodyText(
                                "Celsius (Metric)",
                                textColor = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        DropdownMenuItem(onClick = {
                            onUnitChange("us")
                            onExpandedChange(false)
                            onReloadClick()
                        }) {
                            BodyText(
                                "Fahrenheit (US)",
                                textColor = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                content = content
            )
        }
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

@SuppressLint("UseKtx")
fun getWeatherIconUri(iconName: String, context: Context): Uri {
    val resourceId = when (iconName) {
        "clear-day" -> R.raw.sun_17102813
        "clear-night" -> R.raw.night_17102940
        "cloudy" -> R.raw.clouds_17102874
        "fog" -> R.raw.foggy_17102868
        "hail" -> R.raw.hailstones_17102992
        "partly-cloudy-day" -> R.raw.partly_cloudy_17102931
        "partly-cloudy-night" -> R.raw.cloudy_night_17102854
        "rain" -> R.raw.rain_17102963
        "rain-snow" -> R.raw.hail_17858185
        "rain-snow-showers-day" -> R.raw.hail_17858185
        "rain-snow-showers-night" -> R.raw.hail_17858185
        "showers-day" -> R.raw.showers_day_17103009
        "showers-night" -> R.raw.rain_17102963
        "sleet" -> R.raw.sleet_17103071
        "snow" -> R.raw.snow_19003498
        "snow-showers-day" -> R.raw.snow_17103041
        "snow-showers-night" -> R.raw.snow_17103041
        "thunder" -> R.raw.storm_17102956
        "thunder-rain" -> R.raw.storm_18821397
        "thunder-showers-day" -> R.raw.storm_18998623
        "thunder-showers-night" -> R.raw.storm_18821397
        "wind" -> R.raw.wind_17102829
        else -> R.raw.sun_17102813
    }

    return "android.resource://${context.packageName}/$resourceId".toUri()
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
