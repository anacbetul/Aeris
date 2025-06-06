package com.luci.aeris.utils.navigator

import AuthLayout
import MainLayout
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.luci.aeris.utils.constants.NavigationRoutes
import com.luci.aeris.domain.repository.FirebaseAuthState
import com.luci.aeris.presentation.ui.AddClothes
import com.luci.aeris.presentation.ui.ChooseOutfitScreen
import com.luci.aeris.presentation.ui.Wardrobe
import com.luci.aeris.presentation.viewmodel.ThemeViewModel
import com.luci.aeris.presentation.ui.LoginScreen
import com.luci.aeris.presentation.ui.MainScreen
import com.luci.aeris.presentation.ui.Profile
import com.luci.aeris.presentation.ui.RegisterScreen
import com.luci.aeris.utils.constants.NavigationRoutes.AddClothes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    navigator: Navigator,
    authState: FirebaseAuthState,
    themeViewModel: ThemeViewModel
) {
    val startDestination = if (authState == FirebaseAuthState.Authenticated) {
        NavigationRoutes.Main
    } else {
        NavigationRoutes.Login
    }

    NavHost(navController = navHostController, startDestination = startDestination) {
        // AUTH Screens
        composable(NavigationRoutes.Login) {
            AuthLayout {
                LoginScreen(navigator = navigator)
            }
        }
        composable(NavigationRoutes.Register) {
            AuthLayout {
                RegisterScreen(navigator = navigator)
            }
        }

        // MAIN Screens
        composable(NavigationRoutes.Main) {
            MainLayout(navHostController, navigator) {
                MainScreen(navigator = navigator)
            }
        }
        composable(NavigationRoutes.ChooseOutfit) {
            MainLayout(navHostController, navigator) {
                ChooseOutfitScreen(navigator = navigator)
            }
        }
        composable(NavigationRoutes.AddClothes) {
            MainLayout(navHostController, navigator) {
                AddClothes(navigator = navigator)
            }
        }
        composable(NavigationRoutes.Wardrobe) {
            MainLayout(navHostController, navigator) {
                Wardrobe(navigator = navigator)
            }
        }
        composable(NavigationRoutes.Profile) {
            MainLayout(navHostController, navigator) {
                Profile(navigator = navigator, themeViewModel = themeViewModel)
            }
        }
    }
}
