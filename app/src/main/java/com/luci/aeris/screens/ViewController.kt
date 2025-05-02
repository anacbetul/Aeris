package com.luci.aeris.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.luci.aeris.utils.AppNavGraph
import com.luci.aeris.utils.Navigator

@Composable
fun ViewController(navHostController: NavHostController, navigator: Navigator) {
    Scaffold(
        topBar = { AppBar() },
        bottomBar = {
            BottomNavigationBar(navController = navHostController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavGraph(navHostController = navHostController, navigator = navigator)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ViewControllerPreview() {
//    AerisTheme {
//        ViewController()
//    }
//}