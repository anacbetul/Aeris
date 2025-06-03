package com.luci.aeris.presentation.ui

import Clothes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.luci.aeris.utils.navigator.Navigator

@Composable
fun Wardrobe(
    navigator: Navigator,
    viewModel: WardrobeViewmodel = viewModel()
) {
    val clothesByCategory = viewModel.clothesByCategory.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value

    // SwipeRefresh state
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            viewModel.refresh()  // ViewModel'deki refresh fonksiyonunu çağır
        },
    ) {
        if (isLoading && clothesByCategory.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                clothesByCategory.entries.forEachIndexed { index, (category, clothesList) ->
                    item {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(clothesList) { clothes ->
                                ClothesCard(clothes = clothes)
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

@Composable
fun ClothesCard(clothes: Clothes) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardSize = screenWidth / 5

    Card(
        modifier = Modifier
            .width(cardSize)
            .aspectRatio(1f),
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
