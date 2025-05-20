package com.luci.aeris.utils.navigator

import androidx.navigation.NavController
import androidx.navigation.NavOptions

class Navigator(private val navController: NavController) {

    fun navigate(route: String, popUpTo: String? = null, inclusive: Boolean = false) {
        val options = popUpTo?.let {
            NavOptions.Builder()
                .setPopUpTo(it, inclusive)
                .build()
        }
        navController.navigate(route, options ?: NavOptions.Builder().build())
    }

    fun popBack() {
        navController.popBackStack()
    }

    fun navigateAndClearBackStack(route: String) {
        navController.navigate(route) {
            popUpTo(0)
        }
    }
}
