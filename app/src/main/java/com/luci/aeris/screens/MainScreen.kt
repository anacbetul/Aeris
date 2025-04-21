package com.luci.aeris.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luci.aeris.ui.theme.AerisTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun MainScreen() {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp


        Column(
            //modifier = Modifier.padding()
        ) {
            Card(
                modifier = Modifier
//                            .background(color = MaterialTheme.colorScheme.secondary)
                    .size(screenWidth, screenHeight / 3)
                    .padding(8.dp),

                shape = CardDefaults.outlinedShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) { Text("weather") }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(6) { index ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f), // kare kartlar
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    ) {
//                            Box(
//                                modifier = Modifier.fillMaxSize(),
//                            ) {
//
//                            }
                        Text(
                            text = "Item ",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
        //}

    }



@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    AerisTheme {

        MainScreen()

    }
}

