package com.luci.aeris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.luci.aeris.screens.Profile
import com.luci.aeris.screens.ViewController
import com.luci.aeris.ui.theme.AerisTheme
import com.luci.aeris.utils.AppNavGraph
import com.luci.aeris.utils.Navigator


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AerisTheme {
                val navController = rememberNavController()
                val navigator = remember { Navigator(navController) }

                AppNavGraph(navController = navController, navigator = navigator)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    AerisTheme {
        ViewController()
    }
}