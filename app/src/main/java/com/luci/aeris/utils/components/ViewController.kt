package com.luci.aeris.utils.components


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.luci.aeris.domain.repository.FirebaseAuthRepository
import com.luci.aeris.presentation.viewmodel.ThemeViewModel
import com.luci.aeris.utils.navigator.AppNavGraph
import com.luci.aeris.utils.navigator.Navigator


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewController(navHostController: NavHostController, navigator: Navigator,themeViewModel: ThemeViewModel) {
    val firebaseAuthRepository: FirebaseAuthRepository = FirebaseAuthRepository()
    val authState by firebaseAuthRepository.authState.collectAsState()

    AppNavGraph(navHostController = navHostController, navigator = navigator, authState=authState,themeViewModel= themeViewModel)
}
