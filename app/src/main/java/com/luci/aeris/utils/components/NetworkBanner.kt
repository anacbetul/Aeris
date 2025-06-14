import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.luci.aeris.ConnectivityObserver
import androidx.compose.runtime.getValue


@Composable
fun NetworkStatusBanner(viewModel: NetworkViewModel = hiltViewModel()) {
    val status  by  viewModel.networkStatus.collectAsState()

    when (status) {
        ConnectivityObserver.Status.Available -> {
        }
        ConnectivityObserver.Status.Unavailable,
        ConnectivityObserver.Status.Lost -> {
            Text(
                text = "İnternet bağlantısı yok!",
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .padding(8.dp)
            )
        }
        ConnectivityObserver.Status.Losing -> {
            Text(
                text = "Bağlantı kopmak üzere...",
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Yellow)
                    .padding(8.dp)
            )
        }
    }
}
