package com.luci.aeris.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.GripVertical
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.Plus
import compose.icons.fontawesomeicons.solid.Tshirt


@Composable
fun BottomBar() {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        windowInsets = BottomAppBarDefaults.windowInsets,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.secondary,//secondary secilmeyen//TODO
        actions = {
            IconButton(onClick = { /* do something */ }, modifier = Modifier.weight(1f)) {
                Icon(
                    FontAwesomeIcons.Solid.Home,
                    modifier = Modifier.size(36.dp),
                    contentDescription = "Localized description",

                )
            }
            IconButton(onClick = { /* do something */ }, modifier = Modifier.weight(1f)) {
                Icon(
                    FontAwesomeIcons.Solid.Tshirt,
                    modifier = Modifier.size(36.dp),
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { /* do something */ }, modifier = Modifier.weight(1f)) {
                Icon(
                    FontAwesomeIcons.Solid.Plus,
                    modifier = Modifier.size(36.dp),
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { /* do something */ }, modifier = Modifier.weight(1f)) {
                Icon(
                    FontAwesomeIcons.Solid.GripVertical,
                    modifier = Modifier.size(36.dp),
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { /* do something */ }, modifier = Modifier.weight(1f)) {
                Icon(
                    Icons.Outlined.Person,

                    modifier = Modifier.size(36.dp),
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