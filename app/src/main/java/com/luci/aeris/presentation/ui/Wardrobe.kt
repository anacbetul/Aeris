package com.luci.aeris.presentation.ui

import BodyText
import Clothes
import android.R.attr.text
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.luci.aeris.R
import com.luci.aeris.presentation.viewmodel.WardrobeViewmodel
import com.luci.aeris.utils.constants.StringConstants
import com.luci.aeris.utils.navigator.Navigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Wardrobe(
    navigator: Navigator,
    viewModel: WardrobeViewmodel = viewModel()
) {
    val clothesByCategory = viewModel.clothesByCategory.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    val showDetailSheet = remember { mutableStateOf(false) }
    var selectedClothes by remember { mutableStateOf<Clothes?>(null) }

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    if (isLoading && clothesByCategory.values.all { it.isEmpty() }) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

    } else {
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.refresh()
            },
        ) {
            if (selectedClothes != null && showDetailSheet.value) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showDetailSheet.value = false
                        selectedClothes = null
                    },
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    containerColor = MaterialTheme.colorScheme.background,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.7f)  // ekranın %70'i kadar yükseklik
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        ClothesDetailContent(clothes = selectedClothes!!) {
                            viewModel.deleteClothes(selectedClothes!!.id)
                            showDetailSheet.value = false
                        }
                    }
                }
            }

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
                        BodyText(text = StringConstants.dontHaveClothes)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    clothesByCategory.entries.sortedBy { it.key }.forEachIndexed { index, (category, clothesList) ->
                        if (clothesList.isNotEmpty()) {
                            item {
                                BodyText(
                                    text = category,
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    textColor = MaterialTheme.colorScheme.tertiary
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
                                                ClothesCard(clothes = clothes) {
                                                    selectedClothes = clothes
                                                    showDetailSheet.value = true
                                                }

                                            }
                                            repeat(4 - rowItems.size) {
                                                Spacer(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .aspectRatio(1f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            if (index != clothesByCategory.size - 1) {
                                item {
                                    Divider(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
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
    Box(
        modifier = Modifier
            .width(cardSize)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.White.copy(alpha = 0.2f))
                .blur(16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            if (!clothes.photoPath.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(clothes.photoPath),
                    contentDescription = "Clothes image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.cloudy),
                    contentDescription = "Default image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }
}

@Composable
fun ClothesDetailContent(clothes: Clothes, onDeleteClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onDeleteClick() }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "My Location",
                tint = Color.Red
            )
            Spacer(modifier = Modifier.width(4.dp))
            BodyText("Sil", textColor = Color.Red)

        }
        if (!clothes.photoPath.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(clothes.photoPath),
                contentDescription = "Selected Clothes",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))

        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                BodyText(
                    text = "${StringConstants.category}: ${clothes.type}",
                    textColor = MaterialTheme.colorScheme.tertiary
                )
                BodyText(
                    text = "${StringConstants.suitableCondition} ${
                        clothes.suitableWeather.joinToString(
                            ", "
                        )
                    }",
                    textColor = MaterialTheme.colorScheme.tertiary
                )
                BodyText(
                    text = "${StringConstants.addedOn} ${clothes.dateAdded}",
                    textColor = MaterialTheme.colorScheme.tertiary,
                    fontSize = 12.sp
                )
            }
        }
    }
}


