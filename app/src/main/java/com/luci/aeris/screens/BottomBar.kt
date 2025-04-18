package com.luci.aeris.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun BottomBar() {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        windowInsets = BottomAppBarDefaults.windowInsets,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.secondary,//secondary secilmeyen//TODO
        actions = {
            IconButton(onClick = { /* do something */ },modifier = Modifier.weight(1f)) {
                Icon(Icons.Outlined.Home, contentDescription = "Localized description")
            }
            IconButton(onClick = { /* do something */ },modifier = Modifier.weight(1f)) {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { /* do something */ },modifier = Modifier.weight(1f)) {
                Icon(
                    Icons.Outlined.Favorite,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { /* do something */ },modifier = Modifier.weight(1f)) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = "Localized description",
                )
            }
        },


        )

}

@Preview(showBackground = true)
@Composable
fun BottombarPreview() {
    BottomBar()
}