package com.luci.aeris.utils.components

import BodyText
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.luci.aeris.utils.constants.StringConstants
import com.luci.aeris.ui.theme.AerisTheme
import com.luci.aeris.utils.constants.NavigationRoutes
import com.luci.aeris.utils.navigator.Navigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    navController: NavHostController,
    navigator: Navigator,
) {
    // Şu anki route'u al
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val isProfileScreen = currentRoute == NavigationRoutes.Profile

    CenterAlignedTopAppBar(
        title = {
            BodyText(
                StringConstants.title,
                textAlign = TextAlign.Center, fontSize = 24.sp,
                textColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        navigationIcon = {
            if (isProfileScreen) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Back",

                    )
                }
            }
        },
        actions = {
            if (!isProfileScreen) {
                IconButton(onClick = {
                    navigator.navigate(NavigationRoutes.Profile)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Profile",

                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
