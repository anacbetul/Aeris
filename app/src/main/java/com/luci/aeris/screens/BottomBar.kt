import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.CurtainsClosed
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.luci.aeris.constants.NavigationRoutes
import com.luci.aeris.domain.model.BottomNavItem


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Main", Icons.Default.Home, NavigationRoutes.Main),
        BottomNavItem("Outfit", Icons.Default.Checkroom, NavigationRoutes.ChooseOutfit),
        BottomNavItem("Add", Icons.Default.Add, NavigationRoutes.AddClothes),
        BottomNavItem("Wardrobe", Icons.Default.CurtainsClosed, NavigationRoutes.Wardrobe),
        BottomNavItem("Profile", Icons.Default.Person, NavigationRoutes.Profile),
    )

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .height(75.dp)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                shape = RoundedCornerShape(24.dp)
            ),
        tonalElevation = 3.dp,
        containerColor = Color.Transparent
    ) {
        items.forEach { item ->
            val selected = currentDestination == item.route

            val iconTint by animateColorAsState(
                targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                animationSpec = tween(300)
            )
            val labelColor by animateColorAsState(
                targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                animationSpec = tween(300)
            )
            val backgroundColor by animateColorAsState(
                targetValue = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent,
                animationSpec = tween(300)
            )

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.height(64.dp) // Biraz daha yer ver
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(backgroundColor, shape = RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = iconTint,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Sadece seçiliyse label göster
                        if (selected) {
                            Spacer(modifier = Modifier.height(2.dp))
                            BodyText(
                                text = item.label,
                                textColor = labelColor, // Bu satır eksikti!
                                maxLines = 1
                            )
                        }
                    }
                },

                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = iconTint,
                    unselectedIconColor = iconTint,
                    selectedTextColor = labelColor,
                    unselectedTextColor = labelColor
                )
            )
        }
    }
}
