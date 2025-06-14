import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.luci.aeris.ConnectivityObserver
import com.luci.aeris.utils.components.AirisAlertDialog
import com.luci.aeris.utils.constants.StringConstants
import kotlinx.coroutines.delay


@Composable
fun NetworkStatusDialogHandler(viewModel: NetworkViewModel) {
    val status by viewModel.networkStatus.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity

    var showDialog by remember { mutableStateOf(false) }
    var isRetrying by remember { mutableStateOf(false) }

    // Durumu izleyerek 1.5 saniye boyunca gerçekten bağlantı yoksa dialogu aç
    LaunchedEffect(status) {
        if (!isRetrying) {
            if (status == ConnectivityObserver.Status.Unavailable || status == ConnectivityObserver.Status.Lost) {
                delay(1500)
                if (status == ConnectivityObserver.Status.Unavailable || status == ConnectivityObserver.Status.Lost) {
                    showDialog = true
                }
            } else {
                showDialog = false
            }
        }
    }

    // Loading gösterimi
    if (isRetrying) {
        AlertDialog(
            onDismissRequest = {}, // dismiss edilemesin
            confirmButton = {},
            title = null,
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    BodyText(StringConstants.checkConnection)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        )

        LaunchedEffect(Unit) {
            delay(2000) // Bekleme süresi

            isRetrying = false
            showDialog = status == ConnectivityObserver.Status.Unavailable || status == ConnectivityObserver.Status.Lost
        }
    }

    // Ana dialog
    if (showDialog && !isRetrying) {
        AirisAlertDialog(
            title = StringConstants.connectionError,
            message = StringConstants.connectionErrorMessage,
            confirmText = StringConstants.retry,
            dismissText = StringConstants.exitApp,
            onConfirm = {
                isRetrying = true
                showDialog = false
            },
            onDismiss = {
                activity?.finish()
            },
            iconColor = MaterialTheme.colorScheme.error,
            showDivider = true
        )
    }
}
