package com.luci.aeris.presentation.ui

import android.R.attr.onClick
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.luci.aeris.R
import com.luci.aeris.presentation.viewmodel.ChooseOutfitViewModel
import com.luci.aeris.presentation.viewmodel.WardrobeViewmodel
import com.luci.aeris.presentation.viewmodel.WeatherViewModel
import com.luci.aeris.utils.navigator.Navigator

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ResourceType")

@Composable
fun MainScreen(navigator: Navigator) {
    val viewModel: WeatherViewModel = hiltViewModel()
    val wardrobeViewModel: WardrobeViewmodel = viewModel()
    val chooseOutfitViewModel: ChooseOutfitViewModel = hiltViewModel()
    val selectedDay by viewModel.selectedDay.collectAsState()
    val isRefreshing by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val location by viewModel.location.collectAsState()
    val unitGroup by viewModel.unitGroup.collectAsState()
//    var unitGroup by remember { mutableStateOf("metric") }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    val recommendCategories by chooseOutfitViewModel.recommendedCategories.collectAsState()
    val clothesByCategory = wardrobeViewModel.clothesByCategory.collectAsState().value
    val filteredClothesByCategory = clothesByCategory.filterKeys { it in recommendCategories }
    val isContentReady = recommendCategories.isNotEmpty() && filteredClothesByCategory.isNotEmpty()

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardSize = screenWidth / 2
    val cardHeight = cardSize // zaten kare olduğu için cardSize
    val spacing = 12.dp
    val rowCount = (recommendCategories.size + 1) / 2 // 2 sütun olduğu için
    val gridHeight = (cardHeight + spacing) * rowCount

    LaunchedEffect(Unit) {
        wardrobeViewModel.refresh()
        chooseOutfitViewModel.observeSelectedDay(
            selectedDay = viewModel.selectedDay,
            unitGroup = unitGroup,
            clothesFlow = wardrobeViewModel.clothesByCategory
        )
    }
    Scaffold(
        bottomBar = {
            if (isContentReady) {
                Button(
                    onClick = {
                        navigator.navigate(
                            "choose_outfit?categories=${
                                recommendCategories.joinToString(
                                    ","
                                )
                            }"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text("Show Suggested Outfits")
                }
            }
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.loadWeather(location, unitGroup)
                wardrobeViewModel.refresh()
            },
            modifier = Modifier
                .fillMaxSize()
//                .padding(paddingValues),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    WeatherScreen(viewModel = viewModel)
                }
                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(cardHeight * 2), // sabit yükseklik VERMEN LAZIM
                        verticalArrangement = Arrangement.spacedBy(spacing),
                        horizontalArrangement = Arrangement.spacedBy(spacing),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(recommendCategories.size) { index ->
                            Box(
                                modifier = Modifier
                                    .width(cardSize)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(Color.Gray.copy(alpha = 0.2f))
                                        .blur(16.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                ) {
                                    Column {
                                        val category = recommendCategories[index]
                                        val clothes = filteredClothesByCategory[category]
                                        val firstClothing = clothes?.firstOrNull()

                                        if (firstClothing != null) {
                                            val painter = rememberAsyncImagePainter(
                                                model = firstClothing.photoPath
                                            )

                                            Image(
                                                painter = painter,
                                                contentDescription = firstClothing.type,
                                                modifier = Modifier
                                                    .aspectRatio(1f),
                                                contentScale = ContentScale.Crop
                                            )

                                        } else {
                                            Text(text = "Bu kategoride kıyafet yok.")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


