import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun BodyText(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        modifier = modifier,
        color = textColor,
        textAlign = textAlign,
        fontSize = fontSize,
        fontWeight = fontWeight,
        maxLines = maxLines
    )
}
