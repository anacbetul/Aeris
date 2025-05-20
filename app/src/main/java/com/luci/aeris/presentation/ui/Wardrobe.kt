package com.luci.aeris.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.luci.aeris.R
import com.luci.aeris.utils.navigator.Navigator


val categories: Map<Int, String> by lazy {
    mapOf(
        1 to "Hats",
        2 to "Coats",
        3 to "T-shirts",
        4 to "Pants",
        5 to "Shoes"
    )
}

@Composable
fun Wardrobe(navigator: Navigator) {
    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(categories.size) { index ->
                Text(text = categories[index + 1].toString())
                ItemCard()
            }

        }
    }
}

@Composable
fun ItemCard() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val cardSize = screenWidth / 5

    val dummyList = List(8) { it }
    val rows = dummyList.chunked(5) // Her satırda 4 eleman olacak

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp).fillMaxWidth()
//        contentPadding = PaddingValues(8.dp)
    ) {
        rows.forEach { rowItems ->
            LazyRow() {
                items(rowItems) {item->
                    Card(
                        modifier = Modifier
                            .width(cardSize - 8.dp)
                            .aspectRatio(1f).padding(horizontal = 4.dp),
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
                            Text("$item")
                        }
                    }
                }
            }


        }


    }
}
