package com.luci.aeris.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luci.aeris.R
import com.luci.aeris.utils.Navigator

@Composable
fun ChooseOutfitScreen(navigator: Navigator) {
    var rowCount: Int = 6
    var cardCount: Int = 10
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        ItemCard(10, 10)

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemCard(
    rowCount: Int = 10,
    cardCount: Int = 10
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = screenHeight / 5
    val spacing = 12.dp

    val density = LocalDensity.current
    val paddingHorizontal = with(density) { ((screenHeight - cardWidth) / 2).toPx() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(rowCount) { rowIndex ->
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
                items(cardCount) { index ->
                    Card(
                        modifier = Modifier
                            .width(cardWidth)
                            .aspectRatio(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.tshirt),
                                contentDescription = ""
                            )
//                            Text(
//                                text = "Row $rowIndex\nCard $index",
//                                color = Color.White,
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Medium,
//                                textAlign = TextAlign.Center
//                            )
                        }
                    }
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun ChooseOutfitScreenPreview() {
//    ChooseOutfitScreen()
//}