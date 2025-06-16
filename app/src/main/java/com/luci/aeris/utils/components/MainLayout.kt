import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import com.luci.aeris.ConnectivityObserver
import com.luci.aeris.utils.components.AppBar
import com.luci.aeris.utils.navigator.Navigator
import androidx.compose.runtime.getValue
import com.luci.aeris.utils.constants.NavigationRoutes

@Composable
fun MainLayout(
    navController: NavHostController,
    navigator: Navigator,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)

    val viewModel = remember(viewModelStoreOwner) {
        ViewModelProvider(
            viewModelStoreOwner,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NetworkViewModel(context.applicationContext as Application) as T
                }
            }
        )[NetworkViewModel::class.java]
    }

    val status by viewModel.networkStatus.collectAsState()

    Scaffold(
        topBar = { AppBar(navigator=navigator, navController = navController) },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            when (status) {
                ConnectivityObserver.Status.Losing -> {
                    // Zayıf bağlantı: sadece üst banner göster
                    NetworkStatusBanner()
                }
                ConnectivityObserver.Status.Lost,
                ConnectivityObserver.Status.Unavailable -> {
                    // Bağlantı yok: sadece dialog göster
                    NetworkStatusDialogHandler(viewModel)
                }
                else -> {
                    // Available: hiçbir şey gösterme
                }
            }

            content()
        }
    }
}
