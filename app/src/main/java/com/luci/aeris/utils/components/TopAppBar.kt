package com.luci.aeris.utils.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.luci.aeris.utils.constants.StringConstants
import com.luci.aeris.ui.theme.AerisTheme

//class TopAppBar {
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(

        title = {
            Text(
                StringConstants.title, style =
//                    MaterialTheme.typography.bodyLarge.copy(fontSize = 46.sp),
            TextStyle(textAlign = TextAlign.Center, fontSize = 42.sp),
                modifier = Modifier.fillMaxWidth()
            )
        },
        //modifier = Modifier.size(screenWidth,45.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
//            navigationIconContentColor = MaterialTheme.colorScheme.primary,
//            actionIconContentColor = MaterialTheme.colorScheme.secondary
        ),
        /*navigationIcon = {
            IconButton(onClick = { *//* menü veya geri işlemi *//* }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_background), // veya ic_menu
                    contentDescription = "Back"
                )
            }
        },*/
        /*actions = {
            IconButton(onClick = { *//* ayarlar veya başka bir şey *//* }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Settings"
                )
            }
        }*/
    )
}

//}
@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    AerisTheme {
        AppBar()
    }
}