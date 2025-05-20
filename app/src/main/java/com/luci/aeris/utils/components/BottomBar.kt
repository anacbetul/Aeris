import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.CurtainsClosed
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.luci.aeris.utils.constants.NavigationRoutes
import com.luci.aeris.utils.constants.StringConstants
import com.luci.aeris.domain.model.BottomNavItem


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(StringConstants.main, Icons.Outlined.Home, NavigationRoutes.Main),
        BottomNavItem(StringConstants.outfit, Icons.Outlined.Checkroom, NavigationRoutes.ChooseOutfit),
        BottomNavItem(StringConstants.add, Icons.Outlined.Add, NavigationRoutes.AddClothes),
        BottomNavItem(StringConstants.wardrobe, Icons.Outlined.CurtainsClosed, NavigationRoutes.Wardrobe),
        BottomNavItem(StringConstants.profile, Icons.Outlined.Person, NavigationRoutes.Profile),
    )

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .height(75.dp)
            .background(
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                shape = RoundedCornerShape(24.dp)
            ),
        tonalElevation = 3.dp,
        containerColor = Color.Transparent
    ) {
        items.forEach { item ->
            val selected = currentDestination == item.route

            val iconTint by animateColorAsState(
                targetValue = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                animationSpec = tween(300)
            )
            val labelColor by animateColorAsState(
                targetValue = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
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
                },
                label = {
                    if (selected) {
                        Text(
                            text = item.label,
                            fontSize = 12.sp,
                            color = labelColor,
                            maxLines = 1
                        )
                    }
                },
                alwaysShowLabel = true,
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
