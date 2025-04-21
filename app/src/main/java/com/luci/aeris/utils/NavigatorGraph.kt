package com.luci.aeris.utils

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.luci.aeris.constants.NavigationRoutes
import com.luci.aeris.screens.LoginScreen
import com.luci.aeris.screens.RegisterScreen

@Composable
fun AppNavGraph(navController: NavHostController, navigator: Navigator) {
    NavHost(navController = navController, startDestination = NavigationRoutes.Login) {
        composable(NavigationRoutes.Login) {
            LoginScreen(navigator = navigator)
        }
        composable(NavigationRoutes.Register) {
            RegisterScreen(navigator = navigator)
        }
    }
}

