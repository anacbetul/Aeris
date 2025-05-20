package com.luci.aeris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.luci.aeris.presentation.viewmodel.ThemeViewModel
import com.luci.aeris.screens.ViewController
import com.luci.aeris.ui.theme.AerisTheme
import com.luci.aeris.utils.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDark by themeViewModel.isDarkTheme.collectAsState()
            SideEffect {
                println("Dark mode is now: $isDark") // ya da Log.d()
            }
            AerisTheme(darkTheme = isDark) {
                val navController = rememberNavController()
                val navigator = remember { Navigator(navController) }

                ViewController(navController, navigator,themeViewModel = themeViewModel)
            }
        }
    }
}
