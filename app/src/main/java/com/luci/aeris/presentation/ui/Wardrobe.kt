package com.luci.aeris.presentation.ui

import BodyText
import Clothes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.luci.aeris.R
import com.luci.aeris.presentation.viewmodel.WardrobeViewmodel
import com.luci.aeris.utils.constants.NavigationRoutes
import com.luci.aeris.utils.constants.StringConstants
import com.luci.aeris.utils.navigator.Navigator
import retrofit2.http.Body

@Composable
fun Wardrobe(
    navigator: Navigator,
    viewModel: WardrobeViewmodel = viewModel()
) {
    val clothesByCategory = viewModel.clothesByCategory.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            viewModel.refresh()
        },
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Tüm kıyafet listeleri boş mu?
            val isClothesEmpty = clothesByCategory.values.all { it.isEmpty() }

            if (isClothesEmpty) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        BodyText(
                            text = StringConstants.dontHaveClothes
                        )
                    }
                }

            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    clothesByCategory.entries.forEachIndexed { index, (category, clothesList) ->
                        if (clothesList.isNotEmpty()) {
                            item {
                                BodyText(
                                    text = category,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            item {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    clothesList.chunked(4).forEach { rowItems ->
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            rowItems.forEach { clothes ->
                                                ClothesCard(clothes = clothes, onClick = {
                                                    navigator.navigate("${NavigationRoutes.ClothesDetail}/${clothes.id}")
                                                })
                                            }
                                            repeat(4 - rowItems.size) {
                                                Spacer(modifier = Modifier
                                                    .weight(1f)
                                                    .aspectRatio(1f))
                                            }
                                        }
                                    }
                                }
                            }

                            if (index != clothesByCategory.size - 1) {
                                item {
                                    Divider(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun ClothesCard(clothes: Clothes, onClick: () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardSize = screenWidth / 5

    Card(
        modifier = Modifier
            .width(cardSize)
            .aspectRatio(1f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (!clothes.photoPath.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(clothes.photoPath),
                    contentDescription = "Clothes image",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.cloudy),
                    contentDescription = "Default image",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
