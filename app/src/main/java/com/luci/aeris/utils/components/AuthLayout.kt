import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.luci.aeris.ConnectivityObserver

@Composable
fun AuthLayout(content: @Composable () -> Unit) {
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

    Box(modifier = Modifier.fillMaxSize()) {
        when (status) {
            ConnectivityObserver.Status.Losing -> {
                NetworkStatusBanner()
            }
            ConnectivityObserver.Status.Lost,
            ConnectivityObserver.Status.Unavailable -> {
                NetworkStatusDialogHandler(viewModel)
            }
            else -> {
                // Available: hiçbir şey gösterme
            }
        }

        content()
    }
}
