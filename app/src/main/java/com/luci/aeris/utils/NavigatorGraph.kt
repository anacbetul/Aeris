package com.luci.aeris.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.luci.aeris.constants.NavigationRoutes
import com.luci.aeris.screens.LoginScreen
import com.luci.aeris.screens.RegisterScreen

import com.luci.aeris.screens.*

@Composable
fun AppNavGraph(navHostController: NavHostController, navigator: Navigator) {
    NavHost(navController = navHostController, startDestination = NavigationRoutes.Main) {

        composable(NavigationRoutes.Login) {
            LoginScreen(navigator = navigator)
        }
        composable(NavigationRoutes.Register) {
            RegisterScreen(navigator = navigator)
        }

        // Yeni sekmeler
        composable(NavigationRoutes.Main) {
            MainScreen(navigator = navigator)
        }
        composable(NavigationRoutes.ChooseOutfit) {
            ChooseOutfitScreen(navigator = navigator)
        }
//        composable(NavigationRoutes.AddClothes) {
//            AddClothes(navigator = navigator)
//        }
        composable(NavigationRoutes.Wardrobe) {
            Wardrobe(navigator = navigator)
        }
        composable(NavigationRoutes.Profile) {
            Profile(navigator = navigator)
        }
    }
}

