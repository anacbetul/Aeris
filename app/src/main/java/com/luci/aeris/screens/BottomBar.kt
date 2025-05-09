package com.luci.aeris.screens



import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.luci.aeris.constants.NavigationRoutes

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Main", Icons.Default.Home, NavigationRoutes.Main),
        BottomNavItem("Outfit", Icons.Default.Checkroom, NavigationRoutes.ChooseOutfit),
        BottomNavItem("Add", Icons.Default.Add, NavigationRoutes.AddClothes),
        BottomNavItem("Wardrobe", Icons.Default.CurtainsClosed, NavigationRoutes.Wardrobe),
        BottomNavItem("Profile", Icons.Default.Person, NavigationRoutes.Profile),
    )

    BottomNavigation {

        val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                modifier = Modifier.fillMaxWidth(),
                selectedContentColor =  MaterialTheme.colorScheme.background,
                unselectedContentColor = MaterialTheme.colorScheme.secondary,
//                windowInsets = BottomAppBarDefaults.windowInsets,
//                containerColor = MaterialTheme.colorScheme.background,
//                contentColor = MaterialTheme.colorScheme.secondary,//secondary secilmeyen//TODO
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination == item.route,
                onClick = {
                    if (currentDestination != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)
