import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun HeaderText(
    modifier: Modifier = Modifier,
    text: String,
    centerTitle: Boolean = false,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        color = textColor,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = fontWeight,
        ),
        modifier = modifier.fillMaxWidth(),
        textAlign = if (centerTitle) TextAlign.Center else TextAlign.Start
    )
}
