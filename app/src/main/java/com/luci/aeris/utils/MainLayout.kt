import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.luci.aeris.screens.AppBar
import com.luci.aeris.utils.Navigator

@Composable
fun MainLayout(
    navController: NavHostController,
    navigator: Navigator,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { AppBar() },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}
