package com.luci.aeris.presentation.ui

import Clothes
import android.R.attr.rowCount
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.outlined.RemoveCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.common.collect.Multimaps.index
import com.luci.aeris.R
import com.luci.aeris.presentation.viewmodel.ChooseOutfitViewModel
import com.luci.aeris.presentation.viewmodel.WardrobeViewmodel
import com.luci.aeris.presentation.viewmodel.WeatherViewModel
import com.luci.aeris.utils.constants.StringConstants.Companion.clothes
import com.luci.aeris.utils.navigator.Navigator
import java.time.LocalDate

@Composable
fun ChooseOutfitScreen(
    navigator: Navigator,
    categories: String,
    wardrobeViewModel: WardrobeViewmodel = viewModel(),
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    chooseOutfitViewModel: ChooseOutfitViewModel = hiltViewModel()
) {
//    val chooseOutfitViewModel: ChooseOutfitViewModel = hiltViewModel()
    val recommendCategories = if (categories.isNotEmpty()) categories.split(",") else emptyList()
    val clothesByCategory = wardrobeViewModel.clothesByCategory.collectAsState().value
    val selectedDay by weatherViewModel.selectedDay.collectAsState()
    val filteredClothesByCategory = clothesByCategory.filterKeys { it in recommendCategories }
    val filteredClothesState = remember(categories, clothesByCategory) {
        mutableStateMapOf<String, MutableState<List<Clothes>>>().apply {
            clothesByCategory.filterKeys { it in recommendCategories }.forEach { (key, value) ->
                this[key] = mutableStateOf(value)
            }
        }
    }

    val nonEmptyCategories = filteredClothesState.filter { it.value.value.isNotEmpty() }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        nonEmptyCategories.forEach() { (category, clothesState) ->
            item {
                ItemCard(
                    clothes = clothesState.value,
                    onRemove = { clothesId ->
                        clothesState.value = clothesState.value.filterNot { it.id == clothesId }
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemCard(
    clothes: List<Clothes>,
    onRemove: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = screenHeight / 5
    val spacing = 12.dp

    val density = LocalDensity.current
    val paddingHorizontal = with(density) { ((screenHeight - cardWidth) / 2).toPx() }

    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
        contentPadding = PaddingValues(horizontal = (screenWidth - cardWidth) / 2),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        modifier = Modifier
            .fillMaxWidth()
            .height(cardWidth + 24.dp)
    ) {
        items(clothes.size, key = { clothes[it].id }) { index ->
            val clothingItem = clothes[index]

            Box(
                modifier = Modifier
                    .width(cardWidth)
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
                        Box {
                            Image(
                                painter = rememberAsyncImagePainter(model = clothingItem.photoPath),
                                contentDescription = clothingItem.type,
                                modifier = Modifier.aspectRatio(1f),
                                contentScale = ContentScale.Crop
                            )
                            Icon(
                                imageVector = Icons.Filled.Remove,
                                contentDescription = "Sil",
                                tint = Color.Red,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(6.dp)
                                    .clickable {
                                        onRemove(clothingItem.id)

                                    }
                                    .size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}