package com.luci.aeris.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.luci.aeris.ui.theme.AerisTheme

@Composable
fun ViewController() {
    Scaffold(
        topBar = { AppBar() },
        bottomBar = { BottomBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.tertiary
            ) { Icon(Icons.Outlined.Add, contentDescription = "buton") }
        },

        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
ChooseOutfitScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewControllerPreview() {
    AerisTheme {
        ViewController()
    }
}