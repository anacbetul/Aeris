package com.luci.aeris.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.luci.aeris.domain.repository.FirebaseAuthRepository
import com.luci.aeris.utils.AppNavGraph
import com.luci.aeris.utils.Navigator


@Composable
fun ViewController(navHostController: NavHostController, navigator: Navigator) {
    val firebaseAuthRepository: FirebaseAuthRepository = FirebaseAuthRepository()
    val authState by firebaseAuthRepository.authState.collectAsState()

    AppNavGraph(navHostController = navHostController, navigator = navigator, authState=authState)
}
